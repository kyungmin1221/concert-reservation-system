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

    @OneToMany(mappedBy = "concert")
    private List<ConcertEvent> concertEventList = new ArrayList<>();

    @Builder
    public Concert(String name) {
        this.name = name;
    }
}
