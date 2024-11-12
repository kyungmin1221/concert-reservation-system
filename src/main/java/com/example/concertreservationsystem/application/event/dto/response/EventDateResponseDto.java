package com.example.concertreservationsystem.application.event.dto.response;

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
