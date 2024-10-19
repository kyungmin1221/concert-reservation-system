package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;

import java.util.Optional;

public interface QueueRepository {
    boolean existsByUser(User user);

    Optional<QueueEntry> findByQueueToken(String queueToken);

}
