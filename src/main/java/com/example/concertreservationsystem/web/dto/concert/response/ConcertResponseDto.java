package com.example.concertreservationsystem.web.dto.concert.response;

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
