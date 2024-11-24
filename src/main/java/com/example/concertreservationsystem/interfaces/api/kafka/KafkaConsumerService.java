package com.example.concertreservationsystem.interfaces.api.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void listen(String message) {
        try {
            log.info("Kafka 에서 메시지를 수신 {}", message);
        } catch (Exception e) {
            log.error("kafka 에서 메시지를 수신할 때 예외 발생 {}", e.getMessage());
        }
    }
}
