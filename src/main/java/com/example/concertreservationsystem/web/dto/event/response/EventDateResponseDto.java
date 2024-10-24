package com.example.concertreservationsystem.web.dto.event.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventDateResponseDto {
    private LocalDate eventDate;
    private String concertName;

    public EventDateResponseDto(LocalDate eventDate, String concertName) {
        this.eventDate = eventDate;
        this.concertName = concertName;
    }
}
