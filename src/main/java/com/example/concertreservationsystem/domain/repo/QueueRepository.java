package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.User;

public interface QueueRepository {
    boolean existsByUser(User user);

}
