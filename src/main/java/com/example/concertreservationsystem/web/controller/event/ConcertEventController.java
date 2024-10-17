package com.example.concertreservationsystem.web.controller.event;

import com.example.concertreservationsystem.domain.service.ConcertEventService;
import com.example.concertreservationsystem.domain.service.ConcertService;
import com.example.concertreservationsystem.web.dto.event.request.EventRequestDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class ConcertEventController {

    private final ConcertEventService concertEventService;

    @PostMapping
    private ResponseEntity<EventResponseDto> registerConcertEvent(@RequestBody EventRequestDto requestDto) {
        EventResponseDto responseDto = concertEventService.registerConcertEvent(requestDto);
        return ResponseEntity.ok(responseDto);

    }
}
