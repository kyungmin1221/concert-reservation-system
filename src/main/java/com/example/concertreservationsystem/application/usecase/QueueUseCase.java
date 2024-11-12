package com.example.concertreservationsystem.application.usecase;


import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;
import org.springframework.stereotype.Component;

@Component
public interface QueueUseCase {

    public QueueResponseToken addQueueToUser(String uuid);
}
