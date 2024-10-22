package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.web.dto.user.request.UserPointRequestDto;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPointResponseDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public interface UserUseCase {

    public UserResponseDto registerUser(UserRequestDto requestDto);

    public UserPointResponseDto chargePoint(String token, UserPointRequestDto requestDto);

    public UserPointResponseDto getUserPoint(String token);
}
