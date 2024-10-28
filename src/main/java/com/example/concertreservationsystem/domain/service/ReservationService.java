package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.ReservationUseCase;
import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.*;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.web.dto.event.response.EventDateResponseDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import com.example.concertreservationsystem.web.dto.event.response.EventSeatResponseDto;
import com.example.concertreservationsystem.web.dto.reservation.request.ReservationRequestDto;
import com.example.concertreservationsystem.web.dto.reservation.response.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationUseCase {

    private final QueueRepository queueRepository;
    private final JpaConcertRepository concertRepository;
    private final JpaSeatRepository seatRepository;
    private final JpaReservationRepository reservationRepository;
    private final ConcertEventRepository concertEventRepository;

    @Override
    @Transactional
    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto) {

        // 대기열 토큰 검증 먼저 수행
        User user = validateToken(token);

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 콘서트가 없습니다."));

        Seat seat = seatRepository.findSeatForUpdate(
                requestDto.getSeatNumber(), requestDto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석은 선택할 수 없습니다."));

        // 예약이 불가능한 경우 로직 //
        if(!seat.isAvailable()) {
            throw new IllegalStateException("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
        }

        // 예약 가능 : 예약 상태를 true -> false 변경
        seat.reserve();

        Reservation reservation = Reservation.builder()
                .name(user.getName() + " 의 예약입니다.")
                .reservationDate(LocalDateTime.now())
                .user(user)
                .seat(seat)
                .concert(concert)
                .status(ReservationStatus.ONGOING)
                .build();

        reservationRepository.save(reservation);
        return new ReservationResponseDto(
                requestDto.getConcertName(),
                requestDto.getSeatNumber());
    }

    // 예약 가능한 날짜 및 좌석 조회
    // 날짜와 좌석이 false 인 것 리스트로 조회
    @Override
    public List<EventDateResponseDto> getInfoDate(String token) {
        validateToken(token);
        List<ConcertEvent> concertEvents = concertEventRepository.findAvailableConcertEvents();
        return concertEvents.stream()
                .map(concertEvent -> new EventDateResponseDto(
                        concertEvent.getEventDate(),
                        concertEvent.getConcert().getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventSeatResponseDto> getInfoSeat(String token, Long eventId) {
        validateToken(token);
        List<Seat> seats = seatRepository.findAvailableSeatsByEventId(eventId);
        return seats.stream()
                .map(seat -> new EventSeatResponseDto(
                        seat.getSeatNumber()
                ))
                .collect(Collectors.toList());
    }



    // 일정 시간이 지나면 예약을 취소하는 메서드
    public void cancelReservationStatus() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(5);    // 5분 대기

        // 대기열에서 가장 오래 있던 사람을 대기열에서 내보냄
        List<Reservation> expiredTimeReservation = reservationRepository
                .findByStatusAndReservationDateBefore(ReservationStatus.ONGOING, expiredTime);

        for(Reservation reservation : expiredTimeReservation) {
            changeReservationAvailableStatus(reservation);
        }
    }

    // 예약 취소한 좌석을 예약 가능하도록 바꾸어주고 -> 대기중인 사람의 예약 상태를 대기중으로 변경
    private void changeReservationAvailableStatus(Reservation reservation) {
        reservation.setStatusCanceled();
        Seat seat = reservation.getSeat();
        seat.setAvailable();    // 좌석을 다시 이용가능 하도록 변경

        seatRepository.save(seat);
        reservationRepository.save(reservation);

        // 대기열 첫번째 사람 불러옴
        Optional<QueueEntry> waitingPerson = queueRepository.findFirstByOrderByQueuePositionAsc();

        // 만약 대기열에 첫번째 사람이 존재하면 -> 예약 상태를 가능하게 바꿔줌 -> jpa 저장
        if (waitingPerson.isPresent()) {
            QueueEntry queueEntry = waitingPerson.get();
            User user = queueEntry.getUser();

            Reservation newReservation = Reservation.builder()
                    .name(user.getName() + " 의 예약입니다.")
                    .reservationDate(LocalDateTime.now())
                    .user(user)
                    .seat(seat)
                    .concert(reservation.getConcert())
                    .status(ReservationStatus.ONGOING)
                    .build();

            changQueuePosition(queueEntry.getQueuePosition());
            reservationRepository.save(newReservation);
            queueRepository.delete(queueEntry);
        }
    }

    // 대기열 앞 순서가 나갔을 경우 순서를 앞당기는 메소드
    private void changQueuePosition(Long position) {
        List<QueueEntry> waitingPerson = queueRepository.findByQueuePositionGreaterThan(position);
        for (QueueEntry queueEntry : waitingPerson) {
            queueEntry.setQueuePosition(queueEntry.getQueuePosition());
            queueRepository.save(queueEntry);
        }
    }

    // 대기열 토큰 여부 검증
    public User validateToken(String queueToken) {
        QueueEntry queueEntry = queueRepository.findByQueueToken(queueToken)
                .orElseThrow(() -> new IllegalArgumentException("대기열 토큰이 없습니다."));

        return queueEntry.getUser();
    }

    public boolean isValidToken(String queueToken) {
        return queueRepository.existsByQueueToken(queueToken);
    }

}
