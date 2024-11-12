package com.example.concertreservationsystem.application.event.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventSeatResponseDto {
    private String seatNumber;

    public EventSeatResponseDto(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
