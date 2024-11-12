package com.example.concertreservationsystem.application.usecase;


import com.example.concertreservationsystem.application.concert.dto.request.ConcertRequestDto;
import com.example.concertreservationsystem.application.concert.dto.response.ConcertResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface ConcertUseCase {
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto);

}

