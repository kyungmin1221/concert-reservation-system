package com.example.concertreservationsystem.application.concert.facade;

import com.example.concertreservationsystem.application.concert.dto.request.ConcertRequestDto;
import com.example.concertreservationsystem.application.concert.dto.response.ConcertResponseDto;
import com.example.concertreservationsystem.domain.service.concert.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertFacadeImpl implements ConcertFacade{

    private final ConcertService concertService;

    @Override
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto) {
        return concertService.registerConcert(requestDto);
    }
}
