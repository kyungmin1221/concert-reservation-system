package com.example.concertreservationsystem.application.payment.publisher;

import com.example.concertreservationsystem.domain.model.payment.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPublisherImpl implements PaymentPublisher{

    private final ApplicationEventPublisher eventPublisher;
    @Override
    public void publish(PaymentCompletedEvent event){
        eventPublisher.publishEvent(event);
    }
}
