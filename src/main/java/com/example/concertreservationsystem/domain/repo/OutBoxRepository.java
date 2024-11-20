package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.constant.OutboxStatus;
import com.example.concertreservationsystem.domain.model.PaymentOutBox;

import java.util.List;
import java.util.Optional;

public interface OutBoxRepository {
    List<PaymentOutBox> findAllByStatus(OutboxStatus outboxStatus);

    Optional<PaymentOutBox> save(PaymentOutBox paymentOutBox);
}
