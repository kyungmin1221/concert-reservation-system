package com.example.concertreservationsystem.interfaces.api.kafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private final KafkaProducerService producerService;

    public KafkaController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/send")
    public String sendMessage() {
        producerService.sendMessage("Hello from Spring Kafka");
        return "Message sent to Kafka topic";
    }
}
