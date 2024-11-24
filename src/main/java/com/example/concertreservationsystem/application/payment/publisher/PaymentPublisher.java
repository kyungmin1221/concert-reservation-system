package com.example.concertreservationsystem.application.payment.publisher;

import com.example.concertreservationsystem.domain.model.payment.OutboxEvent;

public interface PaymentPublisher {
    void publish(OutboxEvent event);
}
