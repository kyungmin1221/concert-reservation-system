package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.web.dto.reservation.request.ReservationRequestDto;
import com.example.concertreservationsystem.web.dto.reservation.response.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final QueueRepository queueRepository;
    private final JpaConcertRepository concertRepository;
    private final JpaSeatRepository seatRepository;
    private final JpaReservationRepository reservationRepository;
    @Transactional
    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto) {

        User user = userRepository.findByUuid(requestDto.getUuid())
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        Seat seat = seatRepository.findSeatForUpdate(
                requestDto.getSeatNumber(), requestDto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("해당 좌석은 선택할 수 없습니다."));

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 콘서트가 없습니다."));

        // 예약이 불가능한 경우 로직 //
        if(!seat.isAvailable()) {
            throw new IllegalStateException("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
        }

        // 대기열 토큰 검증
        validateToken(token, user);

        // 예약 가능 : 예약 상태를 true -> false 변경
        seat.reserve();

        Reservation reservation = Reservation.builder()
                .name(user.getName() + " 의 예약입니다.")
                .reservationDate(LocalDateTime.now())
                .user(user)
                .seat(seat)
                .concert(concert)
                .build();

        reservationRepository.save(reservation);
        return new ReservationResponseDto(
                requestDto.getConcertName(),
                requestDto.getSeatNumber());
    }

    // 대기열 토큰 여부 검증
    public void validateToken(String queueToken, User user) {
        QueueEntry queueEntry = queueRepository.findByQueueToken(queueToken)
                .orElseThrow(() -> new IllegalArgumentException("대기열 토큰이 없습니다."));

        if(!queueEntry.getUser().equals(user)) {
            throw new IllegalStateException("유효하지 않은 사용자입니다.");
        }
    }

}
