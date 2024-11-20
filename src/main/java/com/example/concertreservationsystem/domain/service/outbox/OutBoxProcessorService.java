package com.example.concertreservationsystem.domain.service.outbox;

import com.example.concertreservationsystem.domain.constant.OutboxStatus;
import com.example.concertreservationsystem.domain.model.PaymentOutBox;
import com.example.concertreservationsystem.domain.repo.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutBoxProcessorService {

    private final OutBoxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void processOutboxMessages() {
        List<PaymentOutBox> outboxMessages = outboxRepository.findAllByStatus(OutboxStatus.INIT);

        for (PaymentOutBox outbox : outboxMessages) {
            try {
                log.info("카프카 메시지 발생 시작");
                // Kafka에 메시지 발행
                kafkaTemplate.send(outbox.getTopic(), outbox.getPayload()).get();

                // 상태 업데이트
                outbox.setStatusProcessed();
                outboxRepository.save(outbox);

                log.info("Outbox 메시지 발행 성공 후 저장 완료 : {}", outbox.getId());

            } catch (Exception e) {
                log.error("Outbox 메시지 발행 실패: {}", outbox.getId(), e);
                // 상태를 FAILED로 변경하여 재처리 가능하게 함
                outbox.setStatusFailed();
                outboxRepository.save(outbox);
            }
        }
    }
}
