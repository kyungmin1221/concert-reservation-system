package com.example.concertreservationsystem.application.usecase;


import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import com.example.concertreservationsystem.web.dto.concert.response.ConcertResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface ConcertUseCase {
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto);

}

