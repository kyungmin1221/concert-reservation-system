package com.example.concertreservationsystem.domain.service.init.reservation;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Reservation;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitReservationDataService {

    private final UserRepository userRepository;
    private final JpaSeatRepository seatRepository;
    private final JpaReservationRepository reservationRepository;


    @Transactional
    public void generateReservations(int numberOfReservations) {
        List<User> users = userRepository.findAll();
        List<Seat> seats = seatRepository.findAll();
        for (int i = 0; i < numberOfReservations; i++) {
            User user = users.get(i % users.size());
            Seat seat = seats.get(i % seats.size());
            Reservation reservation = Reservation.builder()
                    .name(user.getName() + "의 예약")
                    .reservationDate(LocalDateTime.now())
                    .user(user)
                    .seat(seat)
                    .concert(seat.getConcertEvent().getConcert())
                    .status(ReservationStatus.ONGOING)
                    .build();
            reservationRepository.save(reservation);
        }
    }
}
