package com.example.concertreservationsystem.domain.service.user;

import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.application.user.dto.request.UserPointRequestDto;
import com.example.concertreservationsystem.application.user.dto.request.UserRequestDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPointResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPositionResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final RedisTemplate<String,Object> redisTemplate;


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
    @Transactional
    public UserPointResponseDto chargePoint(String token, UserPointRequestDto requestDto) {

        // 대기열 토큰 검증
        User user = validateAnyToken(token);

        // 잔액 충전
        user.addPoints(requestDto.getPoint());
        userRepository.save(user);

        return new UserPointResponseDto(user.getPoint());
    }

    // 잔액 조회 (대기열 토큰 필수)
    public UserPointResponseDto getUserPoint(String token) {
        User user = validateAnyToken(token);
        return new UserPointResponseDto(user.getPoint());
    }

    // 본인의 대기번호 조회
    public UserPositionResponseDto getUserQueuePosition(String token) {
        // Redis를 이용한 대기열 토큰 검증
        validateAnyToken(token);

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

    // 대기열 토큰 여부 검증
    // 대기열 토큰 검증 메서드
    public User validateToken(String queueToken) {
        // 활성화된 토큰인지 확인
        if (!redisTemplate.opsForSet().isMember("active_tokens", queueToken)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        // 토큰에 연결된 유저 정보 조회
        Object userId = redisTemplate.opsForHash().get("queue:token:" + queueToken, "userId");
        if (userId == null) {
            throw new IllegalArgumentException("토큰에 해당하는 유저 정보를 찾을 수 없습니다.");
        }
        String userIdStr = userId.toString();
        Long checkuserId;

        try {
            checkuserId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("유효하지 않은 ID 형식");
        }

        return userRepository.findById(checkuserId)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
    }

    public User validateAnyToken(String token) {
        // 1. 활성화된 토큰인지 확인
        Boolean isActive = redisTemplate.opsForSet().isMember("active_tokens", token);

        // 2. 대기열에 있는 토큰인지 확인
        Long rank = redisTemplate.opsForZSet().rank("waiting_queue", token);

        if (Boolean.TRUE.equals(isActive) || rank != null) {
            // 토큰에 연결된 유저 정보 조회
            Object userId = redisTemplate.opsForHash().get("queue:token:" + token, "userId");
            if (userId == null) {
                throw new IllegalArgumentException("토큰에 해당하는 유저 정보를 찾을 수 없습니다.");
            }
            String userIdStr = userId.toString();
            Long checkUserId;

            try {
                checkUserId = Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("유효하지 않은 ID 형식입니다.");
            }

            return userRepository.findById(checkUserId)
                    .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        } else {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }
}
