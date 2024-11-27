package com.example.concertreservationsystem.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "concert_events",
        indexes = {
                @Index(name = "idx_concert_events_concert_id_event_date", columnList = "concert_id, event_date")
        }
 )
public class ConcertEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(nullable = false)
    private Long availableSeats;

    @Column
    private Long totalSeats = 50L;

    // 콘서트 날짜
    @Column(nullable = false)
    private LocalDate eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @OneToMany(mappedBy = "concertEvent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    @Builder
    public ConcertEvent(LocalDate eventDate, Concert concert) {
        this.eventDate = eventDate;
        this.totalSeats = 50L;
        this.availableSeats = 50L;
        this.concert = concert;
    }

    public void reserveConcert() {
        this.availableSeats -= 1;
    }

    public void cancelReserveConcert() {
        this.availableSeats += 1;
    }
}
