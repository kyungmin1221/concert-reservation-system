package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 상위모듈
@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    @Override
    Optional<User> findByName(String name);

    @Override
    Optional<User> findByUuid(String uuid);
}
