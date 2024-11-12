package com.example.concertreservationsystem.application.scheduler;

import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenExpirationScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReservationService reservationService;

    @Scheduled(fixedRate = 60000) // 60초마다 실행
    public void cleanupExpiredActiveTokens() {
        int expiredCount = 0;

        // 모든 활성화 토큰을 가져와 만료 여부를 확인
        Set<Object> activeTokens = redisTemplate.opsForSet().members("active_tokens");
        if (activeTokens == null || activeTokens.isEmpty()) {
            return;
        }

        for (Object tokenObj : activeTokens) {
            String token = tokenObj.toString();
            String tokenKey = "active_tokens:" + token;

            // 키 존재 여부 확인 (만료된 경우 키가 없음)
            if (!redisTemplate.hasKey(tokenKey)) {
                // 예약 취소 처리 및 관련 키 삭제
                reservationService.cancelReservationByToken(token);
                expiredCount++;
            }
        }

        // 만료된 토큰 수만큼 대기열에서 새로운 사용자 활성화
        if (expiredCount > 0) {
            activateNewTokensFromQueue(expiredCount);
        }
    }

    private void activateNewTokensFromQueue(int count) {
        Set<Object> tokensToActivate = redisTemplate.opsForZSet().range("waiting_queue", 0, count - 1);

        for (Object tokenObj : tokensToActivate) {
            String token = tokenObj.toString();

            // Active Tokens Set에 추가
            redisTemplate.opsForSet().add("active_tokens", token);

            // 활성화 토큰에 만료 시간 설정
            redisTemplate.opsForValue().set("active_tokens:" + token, "", 10, TimeUnit.MINUTES);

            // 대기열에서 제거
            redisTemplate.opsForZSet().remove("waiting_queue", token);
        }
    }
}

