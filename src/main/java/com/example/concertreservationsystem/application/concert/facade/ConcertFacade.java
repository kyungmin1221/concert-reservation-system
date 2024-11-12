package com.example.concertreservationsystem.application.concert.facade;

import com.example.concertreservationsystem.application.concert.dto.request.ConcertRequestDto;
import com.example.concertreservationsystem.application.concert.dto.response.ConcertResponseDto;

public interface ConcertFacade {

    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto);

}
