package com.example.concertreservationsystem.application.usecase;


import com.example.concertreservationsystem.web.dto.queue.response.QueueResponseToken;
import org.springframework.stereotype.Component;

@Component
public interface QueueUseCase {

    public QueueResponseToken addQueueToUser(String uuid);
}
