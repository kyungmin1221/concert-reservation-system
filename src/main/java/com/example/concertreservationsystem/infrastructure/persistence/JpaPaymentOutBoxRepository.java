package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.constant.OutboxStatus;
import com.example.concertreservationsystem.domain.model.PaymentOutBox;
import com.example.concertreservationsystem.domain.repo.PaymentOutBoxRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPaymentOutBoxRepository extends JpaRepository<PaymentOutBox, Long>, PaymentOutBoxRepository {

    @Override
    List<PaymentOutBox> findAllByStatus(OutboxStatus outboxStatus);

    @Override
    PaymentOutBox save(PaymentOutBox paymentOutBox);

    @Override
    Optional<PaymentOutBox> findById(Long outboxId);
}
