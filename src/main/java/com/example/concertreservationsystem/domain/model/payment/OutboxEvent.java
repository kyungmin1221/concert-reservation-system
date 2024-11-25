package com.example.concertreservationsystem.domain.model.payment;

public class OutboxEvent {
    private final Long outboxId;

    public OutboxEvent(Long outboxId) {
        this.outboxId = outboxId;
    }

    public Long getOutboxId() {
        return outboxId;
    }
}


