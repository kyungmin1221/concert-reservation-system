package com.example.concertreservationsystem.domain.service.seat;

import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final JpaSeatRepository seatRepository;

    @Transactional
    public Seat findSeatForUpdate(String seatNumber, Long eventId) {
        return seatRepository.findSeatForUpdate(seatNumber, eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석은 선택할 수 없습니다."));
    }

    @Transactional
    public void setUnavailable(Seat seat) {
        seat.setUnAvailable();
        seatRepository.save(seat);
    }

    @Transactional
    public void setAvailable(Seat seat) {
        seat.setAvailable();
        seatRepository.save(seat);
    }
}
