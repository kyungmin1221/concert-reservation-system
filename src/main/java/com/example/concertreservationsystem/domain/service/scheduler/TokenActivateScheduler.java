package com.example.concertreservationsystem.domain.service.scheduler;


import com.example.concertreservationsystem.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenActivateScheduler {

    private final ReservationService reservationService;

    // 스케줄러 실행
    @Transactional
    @Scheduled(fixedRate = 60000)  // 60초마다 작업 수행시간과 상관없이 실행
    public void cancelExpiredReservations() {
        log.info("======== cancel 스케쥴러 시작 ================");
        //reservationService.cancelReservationStatus();
        reservationService.activateTokens();
        log.info("======== cancel 스케쥴러 종료 ================");
    }
}

