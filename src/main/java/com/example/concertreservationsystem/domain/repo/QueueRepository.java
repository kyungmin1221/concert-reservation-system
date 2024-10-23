package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface QueueRepository {
    boolean existsByUser(User user);

    Optional<QueueEntry> findByQueueToken(String queueToken);

    Optional<QueueEntry> findFirstByOrderByQueuePositionAsc();

    void delete(QueueEntry queueEntry);

    void deleteByUser(User user);

    List<QueueEntry> findByQueuePositionGreaterThan(Long position);

    QueueEntry save(QueueEntry queueEntry);
}
