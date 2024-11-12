package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.application.event.dto.request.EventRequestDto;
import com.example.concertreservationsystem.application.event.dto.response.EventResponseDto;
import org.springframework.stereotype.Component;


@Component
public interface ConcertEventUseCase {
    public EventResponseDto registerConcertEvent(EventRequestDto requestDto);
}

