package com.example.concertreservationsystem.domain.service.user;

import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.application.user.dto.request.UserPointRequestDto;
import com.example.concertreservationsystem.application.user.dto.request.UserRequestDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPointResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPositionResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final RedisTemplate<String,Object> redisTemplate;

//    @PersistenceContext
//    private EntityManager entityManager;


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
        User user = reservationService.validateAnyToken(token);

        // 잔액 충전
        user.addPoints(requestDto.getPoint());
        userRepository.save(user);

        return new UserPointResponseDto(user.getPoint());
    }

    // 잔액 조회 (대기열 토큰 필수)
    public UserPointResponseDto getUserPoint(String token) {
        User user = reservationService.validateAnyToken(token);
        return new UserPointResponseDto(user.getPoint());
    }

    // 본인의 대기번호 조회
    public UserPositionResponseDto getUserQueuePosition(String token) {
        // Redis를 이용한 대기열 토큰 검증
        reservationService.validateAnyToken(token);

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


//    @Transactional
//    public void generateDummyUsers(int count) {
//        List<User> userList = new ArrayList<>();
//        int batchSize = 1000;
//
//        for (int i = 1; i <= count; i++) {
//            String username = "User" + i;
//            User user = User.builder()
//                    .name(username)
//                    .build();
//            userList.add(user);
//
//            if (i % batchSize == 0) {
//                userRepository.saveAll(userList);
//                userRepository.flush();
//                userList.clear();
//                entityManager.clear();
//            }
//        }
//
//        if (!userList.isEmpty()) {
//            userRepository.saveAll(userList);
//            userRepository.flush();
//            userList.clear();
//            entityManager.clear();
//        }
//    }
//    public long countUsers() {
//        return userRepository.count();
//    }
}
