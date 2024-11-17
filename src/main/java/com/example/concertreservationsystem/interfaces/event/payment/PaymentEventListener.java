package com.example.concertreservationsystem.interfaces.event.payment;

import com.example.concertreservationsystem.domain.model.payment.PaymentCompletedEvent;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {

    private final QueueRepository queueRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("결제 완료 후 이벤트 처리 시작");
        try {
            Thread.sleep(5000);
            queueRepository.deleteByUser(event.user());
            log.info("사용자 ID {}의 토큰 삭제 완료", event.user().getId());
        } catch (Exception e) {
            log.error("결제 이벤트 처리 중 오류 발생", e.getMessage());
        }
    }
}
