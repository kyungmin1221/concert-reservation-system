package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.application.event.dto.response.EventDateResponseDto;
import com.example.concertreservationsystem.application.event.dto.response.EventSeatResponseDto;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.reservation.dto.response.ReservationResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReservationUseCase {

    public ReservationResponseDto rvConcertToUser(Long concertId, String token, ReservationRequestDto requestDto);

    public List<EventDateResponseDto> getInfoDate(String token);

    public List<EventSeatResponseDto> getInfoSeat(String token, Long eventId);
}
