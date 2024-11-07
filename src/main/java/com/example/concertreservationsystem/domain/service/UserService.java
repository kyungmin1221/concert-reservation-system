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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final QueueRepository queueRepository;
    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto requestDto) {

        String username = requestDto.getName();
        if(userRepository.findByName(username).isPresent()) {
            log.error("이미 있는 유저의 이름={}", username);
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
        // Redis를 이용한 대기열 토큰 검증
        User user = reservationService.validateToken(token);

        // 토큰의 대기열 순번 조회
        Long position = redisTemplate.opsForZSet().rank("waiting_queue", token);

        if (position == null) {
            // 토큰이 대기열에 없을 경우 (이미 활성화되었거나 잘못된 토큰)
            throw new IllegalArgumentException("대기열에 존재하지 않는 토큰입니다.");
        }

        // 대기 순번은 0부터 시작하므로, 1을 더하여 사용자에게 표시
        position = position + 1;

        return new UserPositionResponseDto(position);
    }
}
