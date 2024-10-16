package com.example.concertreservationsystem.web.dto.concert.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConcertResponseDto {

    private String name;

    public ConcertResponseDto(String name) {
        this.name = name;
    }
}
