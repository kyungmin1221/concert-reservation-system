package com.example.concertreservationsystem.application.reservation.facade;

import com.example.concertreservationsystem.application.event.dto.response.EventDateResponseDto;
import com.example.concertreservationsystem.application.event.dto.response.EventSeatResponseDto;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.reservation.dto.response.ReservationResponseDto;
import com.example.concertreservationsystem.domain.service.queue.TokenService;
import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationFacadeImpl implements ReservationFacade{

    private final ReservationService reservationService;
    private final TokenService tokenService;

    @Override
    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto) {
        return reservationService.rvConcertToUser(concertId, token, requestDto);
    }

    @Override
    public List<EventDateResponseDto> getInfoDate(String token) {
        return reservationService.getInfoDate(token);
    }

    @Override
    public List<EventSeatResponseDto> getInfoSeat(String token, Long eventId) {
        return reservationService.getInfoSeat(token, eventId);
    }

    @Override
    public void cancelReservationStatus() {
        reservationService.cancelReservationStatus();
    }

    @Override
    public void activateTokens() {
        tokenService.activateTokens();
    }

    @Override
    public void cancelReservationByToken(String token) {
        tokenService.cancelReservationByToken(token);
    }

}
