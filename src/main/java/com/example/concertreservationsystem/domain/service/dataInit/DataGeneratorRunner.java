package com.example.concertreservationsystem.domain.service.dataInit;

import com.example.concertreservationsystem.domain.service.dataInit.concert.ConcertAndEventDataGenerator;
import com.example.concertreservationsystem.domain.service.dataInit.user.UserDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataGeneratorRunner implements CommandLineRunner {

    private final UserDataGenerator userDataGenerator;
    private final ConcertAndEventDataGenerator concertDataGenerator;

    @Override
    public void run(String... args) throws Exception {
        userDataGenerator.generateUsers(100000);
        concertDataGenerator.generateConcerts(1,1);
    }
}
