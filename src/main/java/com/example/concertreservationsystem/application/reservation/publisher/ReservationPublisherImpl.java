package com.example.concertreservationsystem.application.reservation.publisher;

import com.example.concertreservationsystem.domain.model.reservation.ReservationCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationPublisherImpl implements ReservationPublisher{

    private final ApplicationEventPublisher eventPublisher;
    @Override
    public void publish(ReservationCompletedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
