package com.example.concertreservationsystem.web.dto.user.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPointResponseDto {

    private Long point;

    public UserPointResponseDto(Long point) {
        this.point = point;
    }
}
