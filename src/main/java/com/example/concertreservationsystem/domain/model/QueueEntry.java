package com.example.concertreservationsystem.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "queues")
public class QueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Long id;

    @Column(nullable = false)
    private String queueToken;      // 유저가 대기열에 진입할 경우 발급할 토큰(!= 유저 토큰)

    @Column(nullable = false)
    private LocalDateTime joinQueue;    // 대기열 진입 시간

    @Column(nullable = false)
    private Long queuePosition;      // 대기 순번을 위한 객체

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public QueueEntry(String queueToken, LocalDateTime joinQueue, Long queuePosition, User user) {
        this.queueToken = queueToken;
        this.joinQueue = joinQueue;
        this.queuePosition = queuePosition;
        this.user = user;
    }
}
