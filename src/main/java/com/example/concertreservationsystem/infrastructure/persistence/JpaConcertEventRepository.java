package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.repo.ConcertEventRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaConcertEventRepository extends JpaRepository<ConcertEvent, Long>, ConcertEventRepository {
    @Override
    boolean existsByConcertAndEventDate(Concert concert, LocalDate eventDate);

    @Override
    List<ConcertEvent> findByAvailableSeatsGreaterThan(Long minSeats);

    @Override
    @Query("SELECT e FROM ConcertEvent e WHERE e.availableSeats > 0")
    List<ConcertEvent> findAvailableConcertEvents();

}
