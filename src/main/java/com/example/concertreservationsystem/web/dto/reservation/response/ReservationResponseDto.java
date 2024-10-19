package com.example.concertreservationsystem.web.dto.reservation.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponseDto {

    private String concertName;
    private String seatNumber;

    public ReservationResponseDto(String concertName, String seatNumber) {
        this.concertName = concertName;
        this.seatNumber = seatNumber;
    }
}
