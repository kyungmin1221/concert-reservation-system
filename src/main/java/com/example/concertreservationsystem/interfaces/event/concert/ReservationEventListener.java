package com.example.concertreservationsystem.interfaces.event.concert;

import com.example.concertreservationsystem.domain.model.reservation.ReservationCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventListener {
    private final RedisTemplate<String, String> redisTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationCompleted(ReservationCompletedEvent event) {
        log.info("예약 완료 후 예약 이벤트 처리를 시작. ReservationId: {}", event.reservationId());

        try {
            redisTemplate.opsForValue().set("reservation_token:" + event.token(),
                    String.valueOf(event.reservationId()));
          //  throw new IllegalStateException("예약 이벤트 오류 테스트");
        } catch (Exception e) {
            log.error("예약 이벤트 처리 중 오류 발생", e.getMessage());
        }
    }
}
