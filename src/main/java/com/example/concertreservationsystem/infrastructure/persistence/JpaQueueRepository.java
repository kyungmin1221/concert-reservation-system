package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaQueueRepository extends JpaRepository<QueueEntry, Long>, QueueRepository {

    @Override
    boolean existsByUser(User user);
}
