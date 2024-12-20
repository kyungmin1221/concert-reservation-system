package com.example.concertreservationsystem.web.dto.user.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private String name;
    private String uuid;

    public UserResponseDto(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
