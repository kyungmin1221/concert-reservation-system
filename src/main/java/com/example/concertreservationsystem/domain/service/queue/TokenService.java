package com.example.concertreservationsystem.domain.service.queue;

import com.example.concertreservationsystem.domain.model.Reservation;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.service.seat.SeatService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final JpaReservationRepository reservationRepository;
    private final SeatService seatService;

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
        seatService.setAvailable(seat);

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
