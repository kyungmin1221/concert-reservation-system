package com.example.concertreservationsystem.interfaces.api.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "spring-group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
}
