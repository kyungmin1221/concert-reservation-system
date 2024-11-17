package com.example.concertreservationsystem.infrastructure.persistence;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.reservation.Reservation;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.ReservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaReservationRepository extends JpaRepository<Reservation,Long>, ReservationRepository {

    // 결제가 완료되지 않고 일정 시간이 지난 예약 찾기
    @Override
    List<Reservation> findByStatusAndReservationDateBefore(ReservationStatus status,
                                                           LocalDateTime dateTime);

    @Override
    Reservation save(Reservation reservation);

    @Override
    Optional<Reservation> findById(Long reservationId);

    @Override
    List<Reservation> findByUserAndSeatAndConcert(User user, Seat seat, Concert concert);

}
