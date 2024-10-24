package com.example.concertreservationsystem.web.dto.user.response;

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
