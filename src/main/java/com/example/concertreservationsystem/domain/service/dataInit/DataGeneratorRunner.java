package com.example.concertreservationsystem.domain.service.dataInit;

import com.example.concertreservationsystem.domain.service.dataInit.concert.ConcertAndEventDataGenerator;
import com.example.concertreservationsystem.domain.service.dataInit.user.UserDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataGeneratorRunner implements CommandLineRunner {

    private final UserDataGenerator userDataGenerator;
    private final ConcertAndEventDataGenerator concertDataGenerator;

    @Override
    public void run(String... args) throws Exception {
        try {
            userDataGenerator.generateUsers(400);
            concertDataGenerator.generateConcerts(1,1);
        } catch (Exception e) {
            log.error("====더미 데이터 생성 오류====");
            e.printStackTrace();
        }
    }
}
