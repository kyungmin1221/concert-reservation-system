package com.example.concertreservationsystem.web.dto.event.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EventResponseDto {

    private LocalDate eventDate;
    private Long totalSeats;
}
