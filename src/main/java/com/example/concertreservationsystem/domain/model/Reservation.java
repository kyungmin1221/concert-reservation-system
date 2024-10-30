package com.example.concertreservationsystem.domain.model;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDateTime reservationDate;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Builder
    public Reservation(String name, LocalDateTime reservationDate, User user, Seat seat, Concert concert,ReservationStatus status) {
        this.name = name;
        this.reservationDate = reservationDate;
        this.user = user;
        this.seat = seat;
        this.concert = concert;
        this.status = status;
    }

    public void setStatusComplete() {
        this.status = ReservationStatus.COMPLETE;
    }

    public void setStatusCanceled() {
        this.status = ReservationStatus.CANCELED;
    }
}

