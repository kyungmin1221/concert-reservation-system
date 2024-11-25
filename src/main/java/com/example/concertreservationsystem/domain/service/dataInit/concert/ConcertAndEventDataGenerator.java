package com.example.concertreservationsystem.domain.service.dataInit.concert;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.service.init.seat.SeatInitializer;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertRepository;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcertAndEventDataGenerator {

    @Autowired
    private JpaConcertRepository concertRepository;

    @Autowired
    private JpaConcertEventRepository concertEventRepository;

    @Autowired
    private SeatInitializer seatInitializer;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void generateConcerts(int concertCount, int eventCount) {
        Faker faker = new Faker();
        int batchSize = 1000; // 배치 크기 조정
        List<Concert> concerts = new ArrayList<>(batchSize);
        List<ConcertEvent> events = new ArrayList<>(batchSize);

        for (int i = 1; i <= concertCount; i++) {
            Concert concert = Concert.builder()
                    .name(faker.name().fullName())
                    .price(10000L)
                    .build();

            concerts.add(concert);

            if (i % batchSize == 0 || i == concertCount) {
                concertRepository.saveAll(concerts);
                entityManager.flush();
                entityManager.clear();
                concerts.clear();
                System.out.println(i + " concerts 저장");
            }

            for (int j = 1; j <= eventCount; j++) {
                ConcertEvent event = ConcertEvent.builder()
                        .concert(concert)
                        .eventDate(LocalDate.from(LocalDateTime.now().plusDays(j)))
                        .totalSeats(50L)
                        .build();
                concertEventRepository.save(event);
                seatInitializer.initializeSeatsForEvent(event);

                if (i % batchSize == 0 || i == eventCount) {
                    concertEventRepository.saveAll(events);
                    entityManager.flush();
                    entityManager.clear();
                    concerts.clear();
                    System.out.println(i + " concerts event 저장");
                }
            }
        }
    }
}
