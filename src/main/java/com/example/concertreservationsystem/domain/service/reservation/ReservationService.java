package com.example.concertreservationsystem.domain.service.reservation;

import com.example.concertreservationsystem.application.reservation.publisher.ReservationPublisher;
import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.model.reservation.Reservation;
import com.example.concertreservationsystem.domain.model.reservation.ReservationCompletedEvent;
import com.example.concertreservationsystem.domain.repo.*;
import com.example.concertreservationsystem.domain.service.concert.ConcertService;
import com.example.concertreservationsystem.domain.service.queue.QueueService;
import com.example.concertreservationsystem.domain.service.seat.SeatService;
import com.example.concertreservationsystem.domain.service.user.UserService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.application.event.dto.response.EventDateResponseDto;
import com.example.concertreservationsystem.application.event.dto.response.EventSeatResponseDto;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.reservation.dto.response.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService  {

    private final QueueRepository queueRepository;
    private final JpaSeatRepository seatRepository;
    private final JpaReservationRepository reservationRepository;
    private final ConcertEventRepository concertEventRepository;
    private final ConcertService concertService;
    private final UserService userService;
    private final SeatService seatService;
    private final QueueService queueService;
    private final ReservationPublisher reservationPublisher;
    private final RedisTemplate<String,Object> redisTemplate;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "availableConcertDates", key = "#token"),
            @CacheEvict(value = "availableConcertSeats", key = "#token + '_' + #requestDto.eventId")
    })
    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto) {
        log.info("콘서트 예약 로직을 실행합니다");
        // 대기열 토큰 검증 먼저 수행
        User user = userService.validateToken(token);
        Concert concert = concertService.getConcertByName(requestDto.getConcertName());
        Seat seat = seatService.findSeatForUpdate(requestDto.getSeatNumber(), requestDto.getEventId());

        // 예약이 불가능한 경우 로직 //
        if(!seat.isAvailable()) {
            log.error("이미 예약한 유저 존재, 좌석에 접근한 유저 = {} ", user.getName());
            log.error("이미 예약이 되어 있는 좌석 = {}", seat.getSeatNumber());
            throw new IllegalStateException("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
        }
        if(seat.getConcertEvent().getTotalSeats() <=0 ) {
            log.error("예약 가능한 좌석이 없습니다. 현재 좌석 0개 이하");
            throw new IllegalStateException("잔여 좌석이 없습니다.");
        }
        // 예약 가능 : 예약 상태를 true -> false 변경
        seatService.setUnavailable(seat);

        Reservation reservation = createReservation(user, concert, seat);
        reservationRepository.save(reservation);

        redisTemplate.opsForValue().set("reservation_token:"+ token, String.valueOf(reservation.getId()));

        // 이벤트 발행
        reservationPublisher.publish(new ReservationCompletedEvent(
                reservation.getId(),
                token,
                user.getId()
        ));

        return new ReservationResponseDto(
                requestDto.getConcertName(),
                requestDto.getSeatNumber());
    }

    private Reservation createReservation(User user, Concert concert, Seat seat) {
        return Reservation.builder()
                .name(user.getName() + " 의 예약입니다.")
                .reservationDate(LocalDateTime.now())
                .user(user)
                .seat(seat)
                .concert(concert)
                .status(ReservationStatus.ONGOING)
                .build();
    }


    // 예약 가능한 날짜 및 좌석 조회
    // 날짜와 좌석이 false 인 것 리스트로 조회
    @Cacheable(value = "availableConcertDates", key = "#token", unless = "#result.isEmpty()")
    public List<EventDateResponseDto> getInfoDate(String token) {
        log.info("예약 가능한 날짜를 조회하는 로직을 실행합니다. 캐시가 적용되어 있습니다.");
        userService.validateToken(token);
        List<ConcertEvent> concertEvents = concertEventRepository.findAvailableConcertEvents();
        return concertEvents.stream()
                .map(concertEvent -> new EventDateResponseDto(
                        concertEvent.getEventDate(),
                        concertEvent.getConcert().getName()
                ))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "availableConcertSeats", key = "#token + '_' + #eventId" ,unless = "#result.isEmpty()")
    public List<EventSeatResponseDto> getInfoSeat(String token, Long eventId) {
        log.info("예약 가능한 좌석을 조회하는 로직을 실행합니다. 캐시가 적용되어 있습니다.");
        userService.validateToken(token);
        List<Seat> seats = seatRepository.findAvailableSeatsByEventId(eventId);
        return seats.stream()
                .map(seat -> new EventSeatResponseDto(
                        seat.getSeatNumber()
                ))
                .collect(Collectors.toList());
    }


    // 일정 시간이 지나면 예약을 취소하는 메서드
    public void cancelReservationStatus() {
        log.info("일정시간이 지나 예약을 취소합니다.");
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(3);

        List<Reservation> expiredTimeReservation = reservationRepository
                .findByStatusAndReservationDateBefore(ReservationStatus.ONGOING, expiredTime);

        for(Reservation reservation : expiredTimeReservation) {
            changeReservationAvailableStatus(reservation);
        }
    }

    // 예약 취소한 좌석을 예약 가능하도록 바꾸어주고 -> 대기중인 사람의 예약 상태를 대기중으로 변경
    private void changeReservationAvailableStatus(Reservation reservation) {
        log.info("대기중인 손님의 예약 상태를 예약이 가능하도록 변경합니다.");
        reservation.setStatusCanceled();
        Seat seat = reservation.getSeat();

        seatService.setAvailable(seat);    // 좌석을 다시 이용가능 하도록 변경

        reservationRepository.save(reservation);

        // 대기열 첫번째 사람 불러옴
        Optional<QueueEntry> waitingPerson = queueRepository.findFirstByOrderByQueuePositionAsc();

        // 만약 대기열에 첫번째 사람이 존재하면 -> 예약 상태를 가능하게 바꿔줌 -> jpa 저장
        if (waitingPerson.isPresent()) {
            QueueEntry queueEntry = waitingPerson.get();
            User user = queueEntry.getUser();

            Reservation newReservation = createReservation(user,reservation.getConcert(),seat);

            queueService.changQueuePosition(queueEntry.getQueuePosition());
            reservationRepository.save(newReservation);
            queueRepository.delete(queueEntry);
        }
    }

    public Reservation getReservationId(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 예약 번호가 존재하지 않습니다."));
    }

}
