package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.QueueUseCase;
import com.example.concertreservationsystem.web.dto.queue.response.QueueResponseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueUseCase queueUseCase;

    public QueueResponseToken addQueueToUser(String uuid) {
        return queueUseCase.addQueueToUser(uuid);
    }
}
