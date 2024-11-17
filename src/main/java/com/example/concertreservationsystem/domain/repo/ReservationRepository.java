package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.reservation.Reservation;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    // 결제가 완료되지 않고 일정 시간이 지난 예약 찾기
    List<Reservation> findByStatusAndReservationDateBefore(ReservationStatus status, LocalDateTime dateTime);
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long reservationId);

    List<Reservation> findByUserAndSeatAndConcert(User user, Seat seat, Concert concert);

}
