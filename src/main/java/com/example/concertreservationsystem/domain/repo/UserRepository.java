package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// 하위모듈 (DIP 원칙으로 해당 인터페이스를 의존성 주입 해야함)
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByName(String name);

    Optional<User> findByUuid(String uuid);
}
