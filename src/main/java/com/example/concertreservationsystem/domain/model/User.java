package com.example.concertreservationsystem.domain.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long point = 0L;

    @Column(nullable = false, unique = true)
    private String uuid;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<QueueEntry> queueEntryList = new ArrayList<>();

    @Builder
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        this.uuid = (uuid != null) ? uuid : UUID.randomUUID().toString();
        // this.uuid = UUID.randomUUID().toString();
    }

    // 잔액 충전
    public void addPoints(Long point) {
        this.point += point;
    }

    // 예약 시 금액 차감
    public void minusPoints(Long point) {
        if(this.point < point) {
            throw new IllegalStateException("콘서트를 결제할 잔액이 부족합니다. 금액을 충전하세요");
        }
        this.point -= point;
    }

}
