package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.ConcertUseCase;
import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import com.example.concertreservationsystem.web.dto.concert.response.ConcertResponseDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertUseCase concertUseCase;


    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto) {
        return concertUseCase.registerConcert(requestDto);
    }
}
