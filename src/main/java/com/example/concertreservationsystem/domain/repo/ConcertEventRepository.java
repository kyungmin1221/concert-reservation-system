package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;

import java.time.LocalDate;
import java.util.List;

public interface ConcertEventRepository {

    //ConcertEvent save(ConcertEvent concertEvent);
    boolean existsByConcertAndEventDate(Concert concert, LocalDate eventDate);

    // 가용 좌석이 있는 콘서트 이벤트 자동 쿼리 생성
    List<ConcertEvent> findByAvailableSeatsGreaterThan(Long minSeats);

    List<ConcertEvent> findAvailableConcertEvents();
}
