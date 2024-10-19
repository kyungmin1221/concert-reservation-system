package com.example.concertreservationsystem.web.dto.queue.response;

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
