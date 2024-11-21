package com.example.concertreservationsystem.application.reservation.facade;

import com.example.concertreservationsystem.application.event.dto.response.EventDateResponseDto;
import com.example.concertreservationsystem.application.event.dto.response.EventSeatResponseDto;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.reservation.dto.response.ReservationResponseDto;

import java.util.List;

public interface ReservationFacade {

    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto);
    public List<EventDateResponseDto> getInfoDate(String token);
    public List<EventSeatResponseDto> getInfoSeat(String token, Long eventId);
    public void cancelReservationStatus();
    public void activateTokens();
    public void cancelReservationByToken(String token);

}

