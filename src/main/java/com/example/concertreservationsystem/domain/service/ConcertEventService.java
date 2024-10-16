package com.example.concertreservationsystem.domain.service;


import com.example.concertreservationsystem.application.usecase.ConcertEventUseCase;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.web.dto.event.request.EventRequestDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertEventService {

    private final ConcertEventUseCase concertEventUseCase;

    public EventResponseDto registerConcertEvent(EventRequestDto requestDto) {
        return concertEventUseCase.registerConcertEvent(requestDto);
    }
}
