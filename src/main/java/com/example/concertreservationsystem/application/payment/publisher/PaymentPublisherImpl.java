package com.example.concertreservationsystem.application.payment.publisher;

import com.example.concertreservationsystem.domain.model.payment.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPublisherImpl implements PaymentPublisher{

    private final ApplicationEventPublisher eventPublisher;
    @Override
    public void publish(OutboxEvent event){
        eventPublisher.publishEvent(event);
    }
}
