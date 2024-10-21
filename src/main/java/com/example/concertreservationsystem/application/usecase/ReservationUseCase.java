package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.web.dto.reservation.request.ReservationRequestDto;
import com.example.concertreservationsystem.web.dto.reservation.response.ReservationResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface ReservationUseCase {

    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto);

}
