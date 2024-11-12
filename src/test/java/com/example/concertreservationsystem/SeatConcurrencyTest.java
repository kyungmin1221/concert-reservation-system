package com.example.concertreservationsystem;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.repo.ReservationRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.application.user.facade.PaymentService;
import com.example.concertreservationsystem.application.reservation.facade.ReservationService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import static org.junit.jupiter.api.Assertions.*;

import com.example.concertreservationsystem.infrastructure.persistence.JpaQueueRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.application.reservation.dto.request.ReservationRequestDto;
import com.example.concertreservationsystem.application.user.dto.request.UserPaymentRequestDto;
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

@SpringBootTest
public class SeatConcurrencyTest {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JpaSeatRepository seatRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private JpaQueueRepository queueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JpaConcertEventRepository concertEventRepository;

    private String seatNumber = "A1";
    private Concert concert;
    private ConcertEvent concertEvent;
    private User user;

    private String username;
    private Long reservationId;
    private Long concertId;
    private String token;

    @BeforeEach
    void setup() {
        concert = concertRepository.save(new Concert("concert", 1000L));
        concertId = concert.getId();

        concertEvent = concertEventRepository.save(
                new ConcertEvent(LocalDate.now(), 50L, concert));

        Seat seat = Seat.builder()
                .seatNumber(seatNumber)
                .concertEvent(concertEvent)
                .build();
        seatRepository.save(seat);

        // 유저 생성
        user = User.builder()
                .name("testUser1")
                .build();
        user.addPoints(2000L);
        userRepository.save(user);

        username = user.getName();

        QueueEntry queueEntry = queueRepository.save(
                new QueueEntry("token1",
                        LocalDateTime.now(),
                        1L,
                        user));
        queueRepository.save(queueEntry);
        token = queueEntry.getQueueToken();
    }

    @Test
    @DisplayName("동시에 결제를 했을 때 낙관적 락 테스트")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void 동시에_결제를_했을때_낙관적_락_테스트() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 성공한 스레드와 실패한 스레드의 결과를 담을 리스트
        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failList = Collections.synchronizedList(new ArrayList<>());

        // 모든 스레드가 동시에 시작하도록 준비
        CountDownLatch latchReady = new CountDownLatch(threadCount);
        CountDownLatch latchStart = new CountDownLatch(1);

        // 예약 생성
        ReservationRequestDto requestDto = new ReservationRequestDto();
        requestDto.setConcertName(concert.getName());
        requestDto.setSeatNumber(seatNumber);
        requestDto.setEventId(concertEvent.getId());

        // 예약 생성 (rvConcertToUser 메서드가 Reservation 객체를 반환하지 않으므로, 예약 후 조회)
        reservationService.rvConcertToUser(concertId, token, requestDto);

        // 생성된 예약을 조회하여 reservationId 할당
        // 사용자와 좌석, 콘서트 정보를 기준으로 예약 조회
        Reservation createdReservation = reservationRepository.findByUserAndSeatAndConcert(
                        user,
                        seatRepository.findBySeatNumberAndConcertEvent(seatNumber, concertEvent)
                                .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다.")),
                        concert)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservationId = createdReservation.getId();

        System.out.println("=========reservationId==========: " + reservationId);

        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() ->  {
                try {
                    System.out.println("======try 시작============");
                    latchReady.countDown(); // 준비 완료
                    latchStart.await();

                    UserPaymentRequestDto paymentRequestDto = new UserPaymentRequestDto();
                    paymentRequestDto.setReservationId(reservationId);

                    paymentService.paymentConcert(token, paymentRequestDto);
                    successList.add("성공한 결제 " );

                } catch (Exception e) {
                    failList.add("실패한 결제 " );
                }
            });
        }

        latchReady.await(); // 모든 스레드 준비될 때까지 대기
        latchStart.countDown(); // 모든 스레드 시작
        executorService.shutdown();

        executorService.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("성공한 결제 시도 : " + successList);
        System.out.println("실패한 결제 시도 : " + failList);

        // 최신 사용자 정보 재조회
        User latestUser = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없음"));

        // 최신 예약 정보 재조회
        Reservation latestReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없음"));

        // 포인트 검증
        assertEquals(2000L - 1000L, latestUser.getPoint(), "차감 금액이 맞지 않습니다.");

        // 성공한 결제 시도가 정확히 하나인지 검증
        assertEquals(1, successList.size(), "성공한 결제는 한 번이어야 합니다.");
        // 나머지 결제는 실패해야 함
        assertEquals(threadCount - 1, failList.size(), "나머지 결제 시도는 실패해야 합니다.");

        // 예약 상태가 완료되었는지 검증
        assertEquals(ReservationStatus.COMPLETE, latestReservation.getStatus(), "예약 상태가 완료되어야 합니다.");
    }

}
