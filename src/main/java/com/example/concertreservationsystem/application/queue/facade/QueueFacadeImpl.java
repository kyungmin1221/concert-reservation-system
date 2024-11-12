package com.example.concertreservationsystem.application.queue.facade;

import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;
import com.example.concertreservationsystem.domain.service.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueFacadeImpl implements QueueFacade{

    private final QueueService queueService;

    @Override
    public QueueResponseToken addQueueToUser(String uuid) {
        return queueService.addQueueToUser(uuid);
    }
}
