package com.example.concertreservationsystem.domain.model;

import com.example.concertreservationsystem.domain.constant.OutboxStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentOutBox {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;    // INIT, PENDING, PROCESSED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static PaymentOutBox create(String topic, String eventType, String payload) {
        PaymentOutBox paymentOutBox = new PaymentOutBox();
        paymentOutBox.topic = topic;
        paymentOutBox.eventType = eventType;
        paymentOutBox.payload = payload;
        paymentOutBox.status = OutboxStatus.INIT;
        return paymentOutBox;
    }

    public void setStatusProcessed() {
        this.status = OutboxStatus.PROCESSED;
    }
    public void setStatusFailed() {
        this.status = OutboxStatus.FAILED;
    }
}
