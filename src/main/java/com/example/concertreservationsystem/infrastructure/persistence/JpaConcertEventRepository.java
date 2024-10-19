package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.repo.ConcertEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JpaConcertEventRepository extends JpaRepository<ConcertEvent, Long>, ConcertEventRepository {
    @Override
    boolean existsByConcertAndEventDate(Concert concert, LocalDate eventDate);

}
