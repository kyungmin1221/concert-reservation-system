package com.example.concertreservationsystem.api.controller.event;

import com.example.concertreservationsystem.application.event.facade.ConcertEventFacade;
import com.example.concertreservationsystem.application.usecase.ConcertEventUseCase;
import com.example.concertreservationsystem.application.event.dto.request.EventRequestDto;
import com.example.concertreservationsystem.application.event.dto.response.EventResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "콘서트 이벤트 API", description = "콘서트 이벤트 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class ConcertEventController {

    private final ConcertEventFacade concertEventFacade;

    @Operation(
            summary = "콘서트 이벤트 생성",
            description = "",
            parameters = {
                    @Parameter(name = "concertName", description = "콘서트의 이름", example = "ive"),
                    @Parameter(name = "evertDate", description = "콘서트가 열리는 날짜", example = "2024-10-10"),
                    @Parameter(name = "totalSeats", description = "콘서트의 전체 좌석", example = "100"),
            }
    )
    @PostMapping
    private ResponseEntity<EventResponseDto> registerConcertEvent(@RequestBody EventRequestDto requestDto) {
        EventResponseDto responseDto = concertEventFacade.registerConcertEvent(requestDto);
        return ResponseEntity.ok(responseDto);

    }
}
