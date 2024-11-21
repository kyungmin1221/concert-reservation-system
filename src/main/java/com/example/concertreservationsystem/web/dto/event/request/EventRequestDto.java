package com.example.concertreservationsystem.web.dto.event.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventRequestDto {
    private String concertName;
    private LocalDate eventDate;
    private Long totalSeats;
}