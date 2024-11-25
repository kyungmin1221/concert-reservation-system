package com.example.concertreservationsystem.interfaces.event.outbox;

import com.example.concertreservationsystem.domain.model.PaymentOutBox;
import com.example.concertreservationsystem.domain.model.payment.OutboxEvent;
import com.example.concertreservationsystem.domain.repo.PaymentOutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxListener {
    private final PaymentOutBoxRepository paymentOutBoxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOutboxEvent(OutboxEvent event) {
        PaymentOutBox outbox = paymentOutBoxRepository.findById(event.getOutboxId())
                .orElseThrow(() -> new IllegalArgumentException("Outbox 메시지를 찾을 수 없습니다. ID: " + event.getOutboxId()));

        try {
            // Kafka에 메시지 발행
            log.info("Kafka에 메시지 발행");
            kafkaTemplate.send(outbox.getTopic(), outbox.getPayload()).get();

            outbox.setStatusProcessed();
            paymentOutBoxRepository.save(outbox);

            log.info("Outbox 메시지 발행 성공: {}", outbox.getId());
        } catch (Exception e) {
            log.info("Kafka에 메시지 발행 실패");
            // 상태를 FAILED로 변경하여 재시도 가능하게 함
            outbox.setStatusFailed();
            paymentOutBoxRepository.save(outbox);

            log.error("Outbox 메시지 발행 실패 : {}", outbox.getId(), e);

        }
    }
}
