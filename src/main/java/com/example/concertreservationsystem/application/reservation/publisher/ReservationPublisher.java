package com.example.concertreservationsystem.application.reservation.publisher;

import com.example.concertreservationsystem.domain.model.reservation.ReservationCompletedEvent;

public interface ReservationPublisher {
    void publish(ReservationCompletedEvent event);
}
