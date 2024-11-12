package com.example.concertreservationsystem;

import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.domain.service.reservation.ReservationService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PerssimisticTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JpaSeatRepository seatRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JpaConcertEventRepository concertEventRepository;

    private Long concertId = 1L;
    private String seatNumber = "A1";
    private List<String> tokens = new ArrayList<>();

    Concert concert;
    ConcertEvent concertEvent;

    Map<String, String> map = new HashMap<>();

    @BeforeEach
    public void setup() {
        // 콘서트 생성
        concert = concertRepository.save(new Concert("concert", 50000L));
        // 콘서트 이벤트 생성
        concertEvent = concertEventRepository.save(
                new ConcertEvent(LocalDate.now(), 50L, concert));
        Seat seat = new Seat(seatNumber, concertEvent);
        seatRepository.save(seat);

        // QueueEntry 생성 및 토큰 수집
        for (int i = 0; i < 5; i++) {
            User user = userRepository.save(new User((long) i, "User " + i));
            QueueEntry queueEntry = queueRepository.save(new QueueEntry("token" + i, LocalDateTime.now(), (long) i + 1, user));
            System.out.println(queueEntry.getQueueToken());
            tokens.add(queueEntry.getQueueToken()); // token0..4 저장
            map.put(queueEntry.getQueueToken(), user.getName());
        }
    }

    @Test
    @DisplayName("동시에 여러 사람이 동일 좌석 예약을 시도할 때 비관적 락 테스트")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void 동시에_여러_사람이_동일_좌석_예약을_시도할때_비관적_락_테스트() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 성공한 스레드와 실패한 스레드의 결과를 담을 리스트
        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failList = Collections.synchronizedList(new ArrayList<>());

        // 모든 스레드가 동시에 시작하도록 준비
        CountDownLatch latchReady = new CountDownLatch(threadCount);
        CountDownLatch latchStart = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            final String token = tokens.get(i);
            final String username = map.get(token);
            executorService.submit(() -> {
                try {
                    System.out.println("==========Try 문 시작===============");
                    latchReady.countDown(); // 준비 완료
                    latchStart.await(); // 모든 스레드가 준비될 때까지 대기

                    ReservationRequestDto requestDto = new ReservationRequestDto();
                    requestDto.setConcertName(concert.getName());
                    requestDto.setSeatNumber(seatNumber);
                    requestDto.setEventId(concertEvent.getId());
                    System.out.println(requestDto.getConcertName());

                    // 같은 콘서트를 동시 점유하면?
                    reservationService.rvConcertToUser(concert.getId(), token, requestDto);
                    successList.add("성공: " + username);
                } catch (Exception e) {
                    failList.add("실패: " + username);
                }
            });
        }

        latchReady.await(); // 모든 스레드 준비될 때까지 대기
        latchStart.countDown(); // 모든 스레드 시작
        executorService.shutdown();

        // 모든 스레드의 실행이 종료된 후 테스트 결과를 평가하기 위함
        // 이를 통해 테스트가 종료되기 전 모든 예약 시도가 완료되도록 보장
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // 테스트 결과 출력
        System.out.println("성공한 예약 시도: " + successList);
        System.out.println("실패한 예약 시도: " + failList);

        // 검증: 성공한 예약 시도는 오직 하나여야 함
        assertEquals(1, successList.size(), "동시 예약 중 성공한 예약은 하나여야 합니다.");
        assertEquals(threadCount - 1, failList.size(), "나머지 예약 시도는 실패해야 합니다.");
    }
}
