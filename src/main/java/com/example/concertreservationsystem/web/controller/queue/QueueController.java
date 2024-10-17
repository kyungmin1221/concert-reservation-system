package com.example.concertreservationsystem.web.controller.queue;


import com.example.concertreservationsystem.domain.service.QueueService;
import com.example.concertreservationsystem.web.dto.queue.response.QueueResponseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/queues")
public class QueueController {

    private final QueueService queueService;

    @PostMapping
    public ResponseEntity<QueueResponseToken> addQueueToUser(@RequestParam String uuid) {
        QueueResponseToken responseToken = queueService.addQueueToUser(uuid);
        return ResponseEntity.ok(responseToken);
    }
}
