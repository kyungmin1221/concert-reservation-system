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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationCompleted(ReservationCompletedEvent event) {
        log.info("예약 완료 후 예약 이벤트 처리를 시작. ReservationId: {}", event.reservationId());

        try {
            log.info("=== 예약 이벤트 리스너 이벤트 받음 === ", event);
            Thread.sleep(3000L);
            log.info("=== 예약 이벤트 리스너 호출 성공 === ");
        } catch (Exception e) {
            log.error("예약 이벤트 처리 중 오류 발생", e.getMessage());
        }
    }
}
