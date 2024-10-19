package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;

import java.time.LocalDate;

public interface ConcertEventRepository {

    //ConcertEvent save(ConcertEvent concertEvent);
    boolean existsByConcertAndEventDate(Concert concert, LocalDate eventDate);

}
