package com.example.concertreservationsystem.application.concert.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConcertResponseDto {

    private String name;
    private Long price;

    public ConcertResponseDto(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
