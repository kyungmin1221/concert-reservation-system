package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.UserUseCase;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.web.dto.user.request.UserPointRequestDto;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPointResponseDto;
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

    public UserPointResponseDto chargePoint(String token, UserPointRequestDto requestDto) {
        return userUseCase.chargePoint(token, requestDto);
    }

    public UserPointResponseDto getUserPoint(String token){
        return userUseCase.getUserPoint(token);
    }
}
