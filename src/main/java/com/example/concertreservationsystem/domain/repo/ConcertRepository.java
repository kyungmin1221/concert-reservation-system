package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.Concert;
import java.util.Optional;

public interface ConcertRepository {
    Concert save(Concert concert);
    Optional<Concert> findByName(String name);
}
