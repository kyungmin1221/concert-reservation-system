package com.example.concertreservationsystem.application.user.facade;

import com.example.concertreservationsystem.application.user.dto.request.UserPointRequestDto;
import com.example.concertreservationsystem.application.user.dto.request.UserRequestDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPointResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPositionResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserResponseDto;
import com.example.concertreservationsystem.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade{

    private final UserService userService;

    @Override
    public UserResponseDto registerUser(UserRequestDto requestDto) {
        return userService.registerUser(requestDto);
    }
    @Override
    public UserPointResponseDto chargePoint(String token, UserPointRequestDto requestDto){
        return userService.chargePoint(token, requestDto);
    }
    @Override
    public UserPointResponseDto getUserPoint(String token){
        return userService.getUserPoint(token);
    }
    @Override
    public UserPositionResponseDto getUserQueuePosition(String token){
        return userService.getUserQueuePosition(token);
    }
}
