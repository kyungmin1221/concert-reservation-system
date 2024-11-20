package com.example.concertreservationsystem.application.scheduler.token;


import com.example.concertreservationsystem.domain.service.queue.QueueService;
import com.example.concertreservationsystem.domain.service.queue.TokenService;
import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenActivateScheduler {

    private final TokenService tokenService;

    // 스케줄러 실행
    @Transactional
    @Scheduled(fixedRate = 60000)  // 60초마다 작업 수행시간과 상관없이 실행
    public void cancelExpiredReservations() {
        log.info("======== active_tokens 활성화 스케쥴러 실행 시작 ================");
        tokenService.activateTokens();
        log.info("========  active_tokens 활성화 스케쥴러 실행완료 ================");
    }
}

