package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.UserUseCase;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserUseCase userUseCase;

    public UserResponseDto registerUser(UserRequestDto requestDto) {
        return userUseCase.registerUser(requestDto);
    }
}
