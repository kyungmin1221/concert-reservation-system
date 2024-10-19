package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.domain.service.ReservationService;
import com.example.concertreservationsystem.web.dto.user.request.UserPointRequestDto;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPointResponseDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private final ReservationService reservationService;

   @Transactional
   public UserResponseDto registerUser(UserRequestDto requestDto) {

       String username = requestDto.getName();
       if(userRepository.findByName(username).isPresent()) {
           throw new IllegalArgumentException("이미 등록된 유저의 이름입니다.");
       }

       User user = User.builder()
               .name(username)
               .build();

       userRepository.save(user);
       return new UserResponseDto(user.getName(), user.getUuid());
   }


    // 잔액 충전 (대기열 토큰 필수)
    @Transactional
    public UserPointResponseDto chargePoint(String token, UserPointRequestDto requestDto) {

        // 대기열 토큰 검증
        User user = reservationService.validateToken(token);

        // 잔액 충전
        user.addPoints(requestDto.getPoint());
        userRepository.save(user);

        return new UserPointResponseDto(user.getPoint());
    }

    // 잔액 조회 (대기열 토큰 필수)
    public UserPointResponseDto getUserPoint(String token) {
       User user = reservationService.validateToken(token);
       return new UserPointResponseDto(user.getPoint());
    }
}
