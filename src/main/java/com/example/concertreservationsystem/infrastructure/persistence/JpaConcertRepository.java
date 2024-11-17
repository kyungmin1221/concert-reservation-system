package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaConcertRepository  extends JpaRepository<Concert, Long>, ConcertRepository {
    @Override
    Optional<Concert> findByName(String name);

    @Override
    Optional<Concert> findById(Long id);

}
