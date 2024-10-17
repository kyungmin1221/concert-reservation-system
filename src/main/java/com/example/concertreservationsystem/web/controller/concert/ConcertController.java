package com.example.concertreservationsystem.web.controller.concert;

import com.example.concertreservationsystem.domain.service.ConcertService;
import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import com.example.concertreservationsystem.web.dto.concert.response.ConcertResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    /**
     * 콘서트 등록
     * @param requestDto
     * @return
     */
    @PostMapping
    public ResponseEntity<ConcertResponseDto> registerConcert(@RequestBody ConcertRequestDto requestDto) {
        ConcertResponseDto responseDto = concertService.registerConcert(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
