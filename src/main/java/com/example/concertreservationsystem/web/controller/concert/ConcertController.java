package com.example.concertreservationsystem.web.controller.concert;

import com.example.concertreservationsystem.application.usecase.ConcertUseCase;
import com.example.concertreservationsystem.domain.service.ConcertService;
import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import com.example.concertreservationsystem.web.dto.concert.response.ConcertResponseDto;
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

@Tag(name = "콘서트 API", description = "콘서트 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertUseCase concertUseCase;

    /**
     * 콘서트 등록
     * @param requestDto
     * @return
     */
    @Operation(
            summary = "콘서트를 생성",
            description = "콘서트를 생성합니다.",
            parameters = {
                    @Parameter(name = "name", description = "생성할 콘서트의 이름", example = "ive"),
                    @Parameter(name = "price", description = "생성할 콘서트의 가격", example = "1000"),
            }
    )
    @PostMapping
    public ResponseEntity<ConcertResponseDto> registerConcert(@RequestBody ConcertRequestDto requestDto) {
        ConcertResponseDto responseDto = concertUseCase.registerConcert(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
