package com.example.concertreservationsystem.application.event.facade;

import com.example.concertreservationsystem.application.event.dto.request.EventRequestDto;
import com.example.concertreservationsystem.application.event.dto.response.EventResponseDto;

public interface ConcertEventFacade {
    public EventResponseDto registerConcertEvent(EventRequestDto requestDto);

}
