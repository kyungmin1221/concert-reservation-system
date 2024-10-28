package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.PaymentUseCase;
import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.Reservation;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.ReservationRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.web.dto.user.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentUseCase {

    private final JpaReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final QueueRepository queueRepository;
    private final ReservationService reservationService;

    @Override
    @Transactional
    public UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto) {

        Reservation reservation = reservationRepository.findById(requestDto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 예약 번호가 존재하지 않습니다."));

        if(reservation.getStatus() != ReservationStatus.ONGOING) {
            throw new IllegalStateException("현재 예약 대기 상태가 아닙니다.");
        }

        // 유저의 대기열 토큰 검증
        User user = reservationService.validateToken(token);
        Concert concert = reservation.getConcert();
        Long concertPrice = concert.getPrice();

        if(!user.equals(reservation.getUser())) {
            throw new IllegalStateException("대기열 토큰이 유효하지 않거나 다른 유저입니다.");
        }

        // 유저 보유 잔액에서 콘서트 금액을 차감
        user.minusPoints(concertPrice);
        userRepository.save(user);

        // 예약 상태를 완료상태로 변경
        reservation.setStatusComplete();
        reservationRepository.save(reservation);

        // 예약상태에서 완료가 되었으면 대기열에서 OUT
        queueRepository.deleteByUser(user);

        return new UserPaymentResponseDto(
                user.getPoint(),
                reservation.getStatus(),
                "예약 완료");
    }
}
