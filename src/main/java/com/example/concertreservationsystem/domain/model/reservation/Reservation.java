package com.example.concertreservationsystem.domain.model.reservation;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "reservations",
        indexes = {
            @Index(name = "idx_reservation_seat_id", columnList = "seat_id")
        }
)
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

