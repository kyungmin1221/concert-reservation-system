package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Reservation;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.repo.ReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationCancellationScheduler {

    private final ReservationRepository reservationRepository;
    private final JpaSeatRepository seatRepository;

    // 스케줄러 실행
    @Scheduled(fixedRate = 60000)  // 60초마다 실행
    @Transactional
    public void cancelExpiredReservations() {
        // 현재 시간 기준으로 취소할 예약을 찾음 ( 5분 이상 경과한 대기 상태 예약)
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        List<Reservation> expiredReservations = reservationRepository.findByStatusAndReservationDateBefore(ReservationStatus.ONGOING, expirationTime);

        // 예약 취소 처리 (for 문이 최선일지 추후 고민)
        for (Reservation reservation : expiredReservations) {
            reservation.setStatusCanceled();

            // 해당 좌석을 다시 예약 가능 상태로 변경
            Seat seat = reservation.getSeat();
            seat.setAvailable();
            seatRepository.save(seat);

            reservationRepository.save(reservation);
        }
    }
}

