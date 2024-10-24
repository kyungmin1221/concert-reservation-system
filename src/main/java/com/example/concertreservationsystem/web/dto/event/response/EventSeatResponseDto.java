package com.example.concertreservationsystem.web.dto.event.response;

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
