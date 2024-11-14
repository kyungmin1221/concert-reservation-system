package com.example.concertreservationsystem.domain.service.queue;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaQueueRepository;
import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService{

    private final UserRepository userRepository;
    private final JpaQueueRepository queueRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    @Transactional
    public QueueEntry addQueueToUser(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        String userQueueToken = "user_in_queue:" + uuid;

        if(redisTemplate.hasKey(userQueueToken)) {
            throw new IllegalArgumentException("이미 유저가 대기열에 진입");
        }


        String token = generateQueueToken();
        long score = System.currentTimeMillis();

        // 유저 ID와 토큰 매핑 저장
        redisTemplate.opsForValue().set(userQueueToken, token);

        // 대기열에 토큰 추가
        redisTemplate.opsForZSet().add("waiting_queue", token, score);

        // 토큰의 메타데이터 저장 (Hash 사용)
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("userId", user.getId());
        tokenData.put("joinQueue", String.valueOf(score));
        log.info("Token Data being stored in Redis: {}", tokenData);
        redisTemplate.opsForHash().putAll("queue:token:" + token, tokenData);

        Long position = redisTemplate.opsForZSet().rank("waiting_queue", String.valueOf(token));

        QueueEntry entry = QueueEntry.builder()
                .queueToken(token)
                .joinQueue(LocalDateTime.now())
                .queuePosition(position)
                .user(user)
                .build();

        return queueRepository.save(entry);
    }


    // 대기열 토큰을 발급해주는 메서드
    private String generateQueueToken() {
        return UUID.randomUUID().toString();
    }
}