package com.example.concertreservationsystem.api.controller.queue;


import com.example.concertreservationsystem.application.usecase.QueueUseCase;
import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "대기열 API", description = "콘서트 대기열 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/queues")
public class QueueController {

    private final QueueUseCase queueUseCase;

    @Operation(
            summary = "콘서트 대기열 생성",
            description = "콘서트 예약전 대기열에 진입하는 API",
            parameters = {
                    @Parameter(name = "uuid", description = "대기열에 진입할 유저의 uuid", example = "abc-def")
            }
    )
    @PostMapping
    public ResponseEntity<QueueResponseToken> addQueueToUser(@RequestParam String uuid) {
        QueueResponseToken responseToken = queueUseCase.addQueueToUser(uuid);
        return ResponseEntity.ok(responseToken);
    }
}
