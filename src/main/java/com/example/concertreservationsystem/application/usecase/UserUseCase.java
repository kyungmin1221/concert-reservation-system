package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.application.user.dto.request.UserPointRequestDto;
import com.example.concertreservationsystem.application.user.dto.request.UserRequestDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPointResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPositionResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface UserUseCase {

    public UserResponseDto registerUser(UserRequestDto requestDto);

    public UserPointResponseDto chargePoint(String token, UserPointRequestDto requestDto);

    public UserPointResponseDto getUserPoint(String token);

    public UserPositionResponseDto getUserQueuePosition(String token);
}
