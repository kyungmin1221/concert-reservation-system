package com.example.concertreservationsystem.domain.service.reservation;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.*;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaUserRepository;
import com.example.concertreservationsystem.application.event.dto.response.EventDateResponseDto;
import com.example.concertreservationsystem.application.event.dto.response.EventSeatResponseDto;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.reservation.dto.response.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService  {

    private final QueueRepository queueRepository;
    private final JpaConcertRepository concertRepository;
    private final JpaSeatRepository seatRepository;
    private final JpaReservationRepository reservationRepository;
    private final ConcertEventRepository concertEventRepository;
    private final JpaUserRepository userRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "availableConcertDates", key = "#token"),
            @CacheEvict(value = "availableConcertSeats", key = "#token + '_' + #requestDto.eventId")
    })
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
            log.error("이미 예약한 유저 존재, 좌석에 접근한 유저 = {} ", user.getName());
            log.error("이미 예약이 되어 있는 좌석 = {}", seat.getSeatNumber());
            throw new IllegalStateException("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
        }

        // 예약 가능 : 예약 상태를 true -> false 변경
        seat.setUnAvailable();

        Reservation reservation = Reservation.builder()
                .name(user.getName() + " 의 예약입니다.")
                .reservationDate(LocalDateTime.now())
                .user(user)
                .seat(seat)
                .concert(concert)
                .status(ReservationStatus.ONGOING)
                .build();
        reservationRepository.save(reservation);

        redisTemplate.opsForValue().set("reservation_token:"+ token, reservation.getId());

        cleanupAfterReservation(token, String.valueOf(user.getId()));

        return new ReservationResponseDto(
                requestDto.getConcertName(),
                requestDto.getSeatNumber());
    }

    // 예약 가능한 날짜 및 좌석 조회
    // 날짜와 좌석이 false 인 것 리스트로 조회
    @Cacheable(value = "availableConcertDates", key = "#token", unless = "#result.isEmpty()")
    public List<EventDateResponseDto> getInfoDate(String token) {
        log.info("getInfoDate 메서드가 호출.");
        validateToken(token);
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
        log.info("좌석 캐시 확인");
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
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(3);

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
    // 대기열 토큰 검증 메서드
    public User validateToken(String queueToken) {
        // 활성화된 토큰인지 확인
        if (!redisTemplate.opsForSet().isMember("active_tokens", queueToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        // 토큰에 연결된 유저 정보 조회
        String userId = (String) redisTemplate.opsForHash().get("queue:token:" + queueToken, "userId");
        if (userId == null) {
            throw new IllegalArgumentException("토큰에 해당하는 유저 정보를 찾을 수 없습니다.");
        }
        Long checkuserId = Long.parseLong(userId);

        return userRepository.findById(checkuserId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
    }

    public boolean isValidToken(String queueToken) {
        return queueRepository.existsByQueueToken(queueToken);
    }

    // 활성화 토큰 처리 메서드
    public void activateTokens() {
        Set<Object> tokensToActivate = redisTemplate.opsForZSet().range("waiting_queue", 0, 4);

        for (Object tokenObj : tokensToActivate) {
            String token = tokenObj.toString();

            // Active Tokens Set에 추가
            redisTemplate.opsForSet().add("active_tokens", token);

            // 활성화 토큰에 만료 시간 설정 (TTL 적용) - 10분
            redisTemplate.opsForValue().set("active_tokens:" + token, "", 10, TimeUnit.MINUTES);

            // 대기열에서 제거
            redisTemplate.opsForZSet().remove("waiting_queue", token);
        }
    }

    // 만료된 활성화 토큰의 예약을 취소하는 메서드
    public void cancelReservationByToken(String token) {
        // Redis에서 예약 ID 조회
        String reservationIdStr = (String) redisTemplate.opsForValue().get("reservation_token:" + token);
        if (reservationIdStr == null) {
            // 예약이 없을 경우 처리 로직
            return;
        }

        Long reservationId = Long.parseLong(reservationIdStr);

        // 예약 ID로 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 예약 취소 처리
        reservation.setStatusCanceled();
        reservationRepository.save(reservation);

        // 좌석 상태 변경
        Seat seat = reservation.getSeat();
        seat.isAvailable(); // 좌석 상태 변경 메서드
        seatRepository.save(seat);

        // Redis에서 매핑 제거
        redisTemplate.delete("reservation_token:" + token);

        // 활성화 토큰 및 관련 키 삭제
        cleanupAfterReservation(token, null);
    }

    // 예약 완료 또는 취소 후 정리 작업
    private void cleanupAfterReservation(String token, String userUuid) {
        // 활성화 토큰에서 제거
        redisTemplate.opsForSet().remove("active_tokens", token);
        redisTemplate.delete("active_tokens:" + token);

        // 토큰 메타데이터 삭제
        redisTemplate.delete("queue:token:" + token);

        // 유저의 대기열 키 삭제
        if (userUuid != null) {
            redisTemplate.delete("user_in_queue:" + userUuid);
        }
    }
}
