package com.example.concertreservationsystem.domain.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "seats",
        indexes = {
                @Index(name = "idx_seats_seat_number_event_id", columnList = "seat_number, event_id")
        }
)
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

    @OneToOne(mappedBy = "seat", fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private Reservation reservation;

    @Builder
    public Seat(String seatNumber, ConcertEvent concertEvent) {
        this.seatNumber = seatNumber;
        this.concertEvent = concertEvent;
    }

    // 예약을 완료 했을 경우 좌석 예약 여부를 false 로 변경
    public void reserve() {
        this.available = false;
        concertEvent.reserveConcert();
    }

    public void setAvailable() {
        this.available = true;
    }

    public void setUnAvailable() {
        this.available = false;
    }
}
