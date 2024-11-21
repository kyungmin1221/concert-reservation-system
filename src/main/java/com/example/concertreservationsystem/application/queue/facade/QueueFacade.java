package com.example.concertreservationsystem.application.queue.facade;

import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;

public interface QueueFacade {
    public QueueResponseToken addQueueToUser(String uuid);
}
