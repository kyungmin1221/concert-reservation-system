package com.example.concertreservationsystem.domain.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private boolean available = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private ConcertEvent concertEvent;

    @Builder
    public Seat(String seatNumber, ConcertEvent concertEvent) {
        this.seatNumber = seatNumber;
        this.available = true;
        this.concertEvent = concertEvent;
    }

}
