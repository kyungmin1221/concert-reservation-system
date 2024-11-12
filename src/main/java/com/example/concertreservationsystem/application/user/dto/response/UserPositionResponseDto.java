package com.example.concertreservationsystem.application.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPositionResponseDto {
    private Long queuePosition;

    public UserPositionResponseDto(Long queuePosition) {
        this.queuePosition = queuePosition;
    }
}
