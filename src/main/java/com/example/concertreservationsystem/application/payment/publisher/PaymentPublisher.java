package com.example.concertreservationsystem.application.payment.publisher;

import com.example.concertreservationsystem.domain.model.payment.PaymentCompletedEvent;

public interface PaymentPublisher {
    void publish(PaymentCompletedEvent event);
}
