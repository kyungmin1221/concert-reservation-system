package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.constant.OutboxStatus;
import com.example.concertreservationsystem.domain.model.PaymentOutBox;
import com.example.concertreservationsystem.domain.repo.OutBoxRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaOutBoxRepository extends JpaRepository<PaymentOutBox, Long>, OutBoxRepository {

    @Override
    List<PaymentOutBox> findAllByStatus(OutboxStatus outboxStatus);

    @Override
    Optional<PaymentOutBox> save(PaymentOutBox paymentOutBox);
}
