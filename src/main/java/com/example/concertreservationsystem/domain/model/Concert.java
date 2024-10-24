package com.example.concertreservationsystem.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "concerts")
public class Concert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;         // 콘서트 가격

    @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY)
    private List<ConcertEvent> concertEventList = new ArrayList<>();

    @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    @Builder
    public Concert(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
