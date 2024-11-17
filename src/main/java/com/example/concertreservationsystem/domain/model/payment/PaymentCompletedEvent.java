package com.example.concertreservationsystem.domain.model.payment;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.User;

public record PaymentCompletedEvent(
    User user,
    Long reservationId,
    String token,
    ReservationStatus reservationStatus
) {}
