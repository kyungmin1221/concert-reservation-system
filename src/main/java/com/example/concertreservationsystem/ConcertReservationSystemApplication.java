package com.example.concertreservationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConcertReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcertReservationSystemApplication.class, args);
    }

}
