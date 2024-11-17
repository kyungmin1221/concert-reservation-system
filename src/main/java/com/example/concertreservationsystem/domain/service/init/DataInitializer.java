package com.example.concertreservationsystem.domain.service.init;

import com.example.concertreservationsystem.domain.service.init.concert.InitDataConcertAndSeatService;
import com.example.concertreservationsystem.domain.service.init.queue.InitQueueDataService;
import com.example.concertreservationsystem.domain.service.init.reservation.InitReservationDataService;
import com.example.concertreservationsystem.domain.service.init.user.InitUserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final InitUserDataService initUserDataService;
    private final InitDataConcertAndSeatService initDataConcertAndSeatService;
    private final InitQueueDataService initQueueDataService;
    private final InitReservationDataService initReservationDataService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        initUserDataService.generateUsers(10000); // 10,000명으로 조정
//        initDataConcertAndSeatService.generateConcertsAndEvents(1000, 10); // 1,000개 콘서트, 각 콘서트당 10개 이벤트
//        initQueueDataService.generateQueueEntries(10000); // 10,000개로 조정
//        initReservationDataService.generateReservations(10000); // 10,000개로 조정
        System.out.println("===초기화 메서드 정상 실행===");
    }

}
