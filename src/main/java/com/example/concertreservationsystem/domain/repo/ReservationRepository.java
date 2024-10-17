package com.example.concertreservationsystem.domain.repo;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Reservation;
import com.example.concertreservationsystem.domain.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    // 결제가 완료되지 않고 일정 시간이 지난 예약 찾기
    List<Reservation> findByStatusAndReservationDateBefore(ReservationStatus status, LocalDateTime dateTime);
    Reservation save(Reservation reservation);
}
