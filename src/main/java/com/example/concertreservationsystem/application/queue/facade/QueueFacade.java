package com.example.concertreservationsystem.application.queue.facade;

import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;
import com.example.concertreservationsystem.domain.model.QueueEntry;

public interface QueueFacade {
    public QueueEntry addQueueToUser(String uuid);
}
