package com.example.concertreservationsystem.application.scheduler.outbox;

import com.example.concertreservationsystem.domain.service.outbox.OutBoxRetryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutBoxRetryScheduler {

    private final OutBoxRetryService outboxRetryService;

    @Scheduled(fixedDelay = 10000)
    public void runOutboxRetry() {
        log.info("Outbox Retry 실행 시작");
        outboxRetryService.retryFailedMessages();
    }
}
