package com.example.concertreservationsystem.domain.model.reservation;


public record ReservationCompletedEvent(
        Long reservationId,
        String token,
        Long userId
)
{}
