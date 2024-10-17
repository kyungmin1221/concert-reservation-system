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
        this.uuid = UUID.randomUUID().toString();
    }

}
