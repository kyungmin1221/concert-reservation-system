package com.example.concertreservationsystem.application.queue.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueResponseToken {

    private String queueToken;

    private Long queuePosition;

    public QueueResponseToken(String queueToken, Long queuePosition) {
        this.queueToken = queueToken;
        this.queuePosition = queuePosition;
    }
}
