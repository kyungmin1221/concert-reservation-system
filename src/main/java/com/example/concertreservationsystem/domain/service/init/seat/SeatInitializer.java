package com.example.concertreservationsystem.domain.service.init.seat;

import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatInitializer {

    private final JpaSeatRepository seatRepository;

    @Transactional
    public void initializeSeatsForEvent(ConcertEvent concertEvent) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= concertEvent.getTotalSeats(); i++) {
            Seat seat = Seat.builder()
                    .seatNumber(String.valueOf(i))
                    .concertEvent(concertEvent)
                    .build();
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }
}
