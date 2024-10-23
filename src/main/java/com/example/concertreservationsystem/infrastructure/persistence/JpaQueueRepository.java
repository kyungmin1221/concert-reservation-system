package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaQueueRepository extends JpaRepository<QueueEntry, Long>, QueueRepository {

    @Override
    boolean existsByUser(User user);

    @Override
    Optional<QueueEntry> findByQueueToken(String queueToken);

    @Override
    Optional<QueueEntry> findFirstByOrderByQueuePositionAsc();

    @Override
    void delete(QueueEntry queueEntry);

    @Override
    void deleteByUser(User user);

    @Override
    List<QueueEntry> findByQueuePositionGreaterThan(Long position);

    @Override
    QueueEntry save(QueueEntry queueEntry);

}
