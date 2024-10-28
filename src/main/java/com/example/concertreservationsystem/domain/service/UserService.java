package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.UserUseCase;
import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.web.dto.user.request.UserPointRequestDto;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPointResponseDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPositionResponseDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final QueueRepository queueRepository;

    @Override
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
    @Override
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
    @Override
    public UserPointResponseDto getUserPoint(String token) {
        User user = reservationService.validateToken(token);
        return new UserPointResponseDto(user.getPoint());
    }

    // 본인의 대기번호 조회
    @Override
    public UserPositionResponseDto getUserQueuePosition(String token) {
        QueueEntry queueEntry = queueRepository.findByQueueToken(token)
                .orElseThrow(() -> new IllegalArgumentException("대기열 토큰이 유효하지 않습니다."));

        return new UserPositionResponseDto(queueEntry.getQueuePosition());
    }
}
