package com.example.concertreservationsystem.web.controller.reservation;

import com.example.concertreservationsystem.application.usecase.ReservationUseCase;
import com.example.concertreservationsystem.web.dto.reservation.request.ReservationRequestDto;
import com.example.concertreservationsystem.web.dto.reservation.response.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;


    /**
     * 콘서트 예약
     * @param concertId
     * @param requestDto
     * @return
     */
    @PostMapping("/{concertId}")
    public ResponseEntity<ReservationResponseDto> rvConcertToUser(@PathVariable Long concertId,
                                                                  @RequestParam String token,
                                                                  @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto responseDto = reservationUseCase.rvConcertToUser(concertId, token, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
