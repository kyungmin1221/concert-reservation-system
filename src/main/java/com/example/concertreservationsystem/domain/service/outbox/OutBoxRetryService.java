package com.example.concertreservationsystem.domain.service.outbox;

import com.example.concertreservationsystem.domain.constant.OutboxStatus;
import com.example.concertreservationsystem.domain.model.PaymentOutBox;
import com.example.concertreservationsystem.domain.repo.PaymentOutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutBoxRetryService {

    private final PaymentOutBoxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void retryFailedMessages() {
        List<PaymentOutBox> failedMessages = outboxRepository.findAllByStatus(OutboxStatus.FAILED);

        for (PaymentOutBox outbox : failedMessages) {
            try {
                log.info("outbox 메시지 재발행 시작");
                kafkaTemplate.send(outbox.getTopic(), outbox.getPayload()).get();

                outbox.setStatusProcessed();
                outboxRepository.save(outbox);

                log.info("outbox 메시지 재발행 성공: {}", outbox.getId());

            } catch (Exception e) {
                log.error("outbox 메시지 재발행 실패: {}", outbox.getId(), e);
            }
        }
    }
}
