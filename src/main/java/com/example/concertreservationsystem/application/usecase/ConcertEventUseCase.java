package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.web.dto.event.request.EventRequestDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import org.springframework.stereotype.Component;


@Component
public interface ConcertEventUseCase {
    public EventResponseDto registerConcertEvent(EventRequestDto requestDto);
}

