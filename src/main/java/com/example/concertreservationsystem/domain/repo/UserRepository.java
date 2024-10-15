package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByName(String name);
}
