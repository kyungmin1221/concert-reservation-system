package com.example.concertreservationsystem.application.scheduler.outbox;

import com.example.concertreservationsystem.domain.service.outbox.OutBoxProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxScheduler {

    private final OutBoxProcessorService outBoxProcessorService;

    @Scheduled(fixedRate = 3000)
    public void runOutBoxProcessor() {
        log.info("OutBox 프로세서 실행 시작 !");
        outBoxProcessorService.processOutboxMessages();
    }
}
