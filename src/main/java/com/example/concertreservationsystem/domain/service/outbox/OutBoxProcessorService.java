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
public class OutBoxProcessorService {

    private final PaymentOutBoxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void processOutboxMessages() {
        List<PaymentOutBox> outboxMessages = outboxRepository.findAllByStatus(OutboxStatus.INIT);

        for (PaymentOutBox outbox : outboxMessages) {
            try {
                processSingleOutboxMessage(outbox);
            } catch (Exception e) {
                log.error("OutBox 처리 중 오류 발생");
            }
        }
    }

    // processSingleOutboxMessage() 메서드로 트랜잭션을 분리 => 각 메시지 처리를 별도의 트랜잭션으로 관리한다는 의미
    // 하나의 메시지를 처리하는 동안 예외가 발생하면 ? -> 그 메세지 처리에 대한 트랜잭션만!! 롤백
    // 만약 processOutboxMessages() 메서드 자체에 트랜잭션이 걸려있지 않기 때문에, 루프를 통해 실패해도 다음 메시지를 처리할 수 있음
    @Transactional
    public void processSingleOutboxMessage(PaymentOutBox outbox) {
        try {
            log.info("카프카 메시지 발생 시작");
            // Kafka에 메시지 발행
            kafkaTemplate.send(outbox.getTopic(), outbox.getPayload()).get();

            // 상태 업데이트
            outbox.setStatusProcessed();
            outboxRepository.save(outbox);

            log.info("Outbox 메시지 발행 성공: {}", outbox.getId());

        } catch (Exception e) {
            log.error("Outbox 메시지 발행 실패: {}", outbox.getId(), e);
            // 상태를 FAILED로 변경하여 재처리 가능하게 함
            outbox.setStatusFailed();
            outboxRepository.save(outbox);
        }
    }
}
