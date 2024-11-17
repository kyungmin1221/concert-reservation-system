package com.example.concertreservationsystem.domain.service.payment;

import com.example.concertreservationsystem.domain.model.payment.PaymentCompletedEvent;
import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.reservation.Reservation;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.domain.service.user.UserService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.application.payment.dto.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.application.payment.dto.response.UserPaymentResponseDto;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final JpaReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final QueueRepository queueRepository;
    private final ReservationService reservationService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto) {

        // 유저의 대기열 토큰 검증
        User user = userService.validateToken(token);

        Reservation reservation = reservationService.getReservationId(requestDto.getReservationId());
        validateReservation(reservation, user);

        Concert concert = reservation.getConcert();
        Long concertPrice = concert.getPrice();

        try {
            // 유저 보유 잔액에서 콘서트 금액을 차감
            user.minusPoints(concertPrice);
            userRepository.save(user);

            // 예약 상태를 완료상태로 변경
            reservation.setStatusComplete();
            reservationRepository.save(reservation);

           eventPublisher.publishEvent(new PaymentCompletedEvent(
                   user,
                   reservation.getId(),
                   token,
                   reservation.getStatus()
           ));

            return new UserPaymentResponseDto(
                    user.getPoint(),
                    reservation.getStatus(),
                    "예약 완료");
        } catch (OptimisticLockException e) {
            log.error("낙관적 락 예외 발생: {}", e.getMessage());
            throw new IllegalStateException("동시 결제 시도가 감지되었습니다. 다시 시도해주세요.");
        }

    }

    private void validateReservation(Reservation reservation, User user) {
        if(reservation.getStatus() != ReservationStatus.ONGOING) {
            log.error("예약중인 상태가 아님");
            throw new IllegalStateException("현재 예약 대기 상태가 아닙니다.");
        }

        if(!user.equals(reservation.getUser())) {
            log.error("예약을 한 유저가 맞는지 확인 필요 = {}", user.getName());
            throw new IllegalStateException("대기열 토큰이 유효하지 않거나 다른 유저입니다.");
        }
    }
}
