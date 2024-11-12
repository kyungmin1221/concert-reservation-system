package com.example.concertreservationsystem.api.controller.reservation;

import com.example.concertreservationsystem.application.reservation.facade.ReservationFacade;
import com.example.concertreservationsystem.application.usecase.ReservationUseCase;
import com.example.concertreservationsystem.application.event.dto.response.EventDateResponseDto;
import com.example.concertreservationsystem.application.event.dto.response.EventSeatResponseDto;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.reservation.dto.response.ReservationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "콘서트 예약 API", description = "콘서트 예약 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationFacade reservationFacade;


    /**
     * 콘서트 예약
     * @param concertId
     * @param requestDto
     * @return
     */
    @Operation(
            summary = "콘서트 예약 생성",
            description = "콘서트 예약 API",
            parameters = {
                    @Parameter(name = "concertName", description = "예약할 콘서트의 이름", example = "ive"),
                    @Parameter(name = "eventId", description = "예약할 콘서트 이벤트 ID", example = "1"),
                    @Parameter(name = "seatNumber", description = "예약할 콘서트의 좌석 번호", example = "1")
            }
    )
    @PostMapping("/{concertId}")
    public ResponseEntity<ReservationResponseDto> rvConcertToUser(@PathVariable Long concertId,
                                                                  @RequestParam String token,
                                                                  @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto responseDto = reservationFacade.rvConcertToUser(concertId, token, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            summary = "콘서트 예약 가능 날짜 조회",
            description = "콘서트 예약 API"
    )
    @GetMapping("/dates")
    public ResponseEntity<List<EventDateResponseDto>> getInfoDateAndSeat(@RequestParam String token) {
        List<EventDateResponseDto> responseDtos = reservationFacade.getInfoDate(token);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(
            summary = "콘서트 예약 가능 좌석 조회",
            description = "콘서트 예약 API"
    )
    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<EventSeatResponseDto>> getInfoSeat(@RequestParam String token,
                                                                  @PathVariable Long eventId) {
        List<EventSeatResponseDto> responseDtos = reservationFacade.getInfoSeat(token, eventId);
        return ResponseEntity.ok(responseDtos);
    }
}
