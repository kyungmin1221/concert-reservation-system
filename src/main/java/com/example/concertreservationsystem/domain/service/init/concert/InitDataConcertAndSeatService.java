package com.example.concertreservationsystem.domain.service.init.concert;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.service.init.seat.SeatInitializer;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InitDataConcertAndSeatService {

    private final ConcertRepository concertRepository;
    private final JpaConcertEventRepository concertEventRepository;
    private final SeatInitializer seatInitializer;


    @Transactional
    public void generateConcertsAndEvents(int numberOfConcerts, int eventsPerConcert) {
        for (int i = 1; i <= numberOfConcerts; i++) {
            Concert concert = Concert.builder()
                    .name("Concert " + i)
                    .price(10000L)
                    .build();
            concertRepository.save(concert);

            for (int j = 1; j <= eventsPerConcert; j++) {
                ConcertEvent event = ConcertEvent.builder()
                        .concert(concert)
                        .eventDate(LocalDate.from(LocalDateTime.now().plusDays(j)))
                        .totalSeats(50L)
                        .build();
                concertEventRepository.save(event);
                seatInitializer.initializeSeatsForEvent(event);
            }
        }
    }
}
