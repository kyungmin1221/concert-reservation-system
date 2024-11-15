package com.example.concertreservationsystem.application.queue.dto;

import com.example.concertreservationsystem.domain.model.QueueEntry;
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

    // factory method 로 dto 생성
    public static QueueResponseToken from(QueueEntry entry) {
        return new QueueResponseToken(entry.getQueueToken(), entry.getQueuePosition());
    }

}
