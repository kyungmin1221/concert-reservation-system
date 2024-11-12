package com.example.concertreservationsystem.application.event.facade;

import com.example.concertreservationsystem.application.event.dto.request.EventRequestDto;
import com.example.concertreservationsystem.application.event.dto.response.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertEventFacadeImpl implements ConcertEventFacade{

    private final ConcertEventService concertEventService;

    @Override
    public EventResponseDto registerConcertEvent(EventRequestDto requestDto) {
        return concertEventService.registerConcertEvent(requestDto);
    }
}
