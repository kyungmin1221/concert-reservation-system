package com.example.concertreservationsystem;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.ReservationRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.domain.service.PaymentService;
import com.example.concertreservationsystem.domain.service.ReservationService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import static org.junit.jupiter.api.Assertions.*;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.web.dto.reservation.request.ReservationRequestDto;
import com.example.concertreservationsystem.web.dto.reservation.response.ReservationResponseDto;
import com.example.concertreservationsystem.web.dto.user.request.UserPaymentRequestDto;
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
    private QueueRepository queueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JpaConcertEventRepository concertEventRepository;
    private Long concertId = 1L;
    private String seatNumber = "A1";
    // private List<String> tokens = new ArrayList<>();
    Concert concert;
    ConcertEvent concertEvent;
    User user;

    private String username;
    private Long reservationId;
    private String token;

    @BeforeEach
    void setup() {
        // 콘서트 생성
        concert = concertRepository.save(new Concert("concert", 1000L));
        concertId = concert.getId();
        // 콘서트 이벤트 생성
        concertEvent = concertEventRepository.save(
                new ConcertEvent(LocalDate.now(), 50L, concert));
//        Seat seat = new Seat(seatNumber, concertEvent);
//        seatRepository.save(seat);
//
//        User user = userRepository.save(new User(null, "testUser1"));
//        user.addPoints(10000L);
//        username = user.getName();
        // 좌석 생성 및 초기 상태 설정
        Seat seat = Seat.builder()
                .seatNumber(seatNumber)
                .concertEvent(concertEvent)
                .build();
        seatRepository.save(seat);

        // 유저 생성
        user = User.builder()
                .name("testUser1")
                .build();
        userRepository.save(user);
        user.addPoints(10000L);
        userRepository.save(user); // 변경 사항 저장

        username = user.getName();

        QueueEntry queueEntry = queueRepository.save(new QueueEntry("token1", LocalDateTime.now(), 1L, user));
        token = queueEntry.getQueueToken();

    }
    @Test
    @DisplayName("동시에 결제를 했을 때 낙관적 락 테스트")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void 동시에_결제를_했을떄_낙관적_락_테스트() throws InterruptedException{
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 성공한 스레드와 실패한 스레드의 결과를 담을 리스트
        List<String> successList = Collections.synchronizedList(new ArrayList<>());
        List<String> failList = Collections.synchronizedList(new ArrayList<>());

        // 모든 스레드가 동시에 시작하도록 준비
        CountDownLatch latchReady = new CountDownLatch(threadCount);
        CountDownLatch latchStart = new CountDownLatch(1);

        ReservationRequestDto requestDto = new ReservationRequestDto();
        requestDto.setConcertName(concert.getName());
        requestDto.setSeatNumber(seatNumber);
        requestDto.setEventId(concertEvent.getId());
//        reservationService.rvConcertToUser(concertId,token,requestDto);
        ReservationResponseDto reservationResponse = reservationService.rvConcertToUser(concertId, token, requestDto);

        reservationId = reservationResponse.getId();

        System.out.println("=========reservationId==========");

        for(int i=0; i<threadCount; i++) {
            executorService.submit(() ->  {
                try {
                    System.out.println("======try 시작============");
                    latchReady.countDown(); // 준비 완료
                    latchStart.await();

                    UserPaymentRequestDto paymentRequestDto = new UserPaymentRequestDto();
                    paymentRequestDto.setReservationId(reservationId);

                    paymentService.paymentConcert(token, paymentRequestDto);
                    successList.add("성공한 결제");

                }catch (Exception e) {
                    failList.add("실패한 결제 ");
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 최신 예약 정보 재조회
        Reservation latestReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // 포인트 검증
        assertEquals(10000L - 1000L, latestUser.getPoint(), "사용자의 포인트가 올바르게 차감되어야 합니다.");

        // 성공한 결제 시도가 정확히 하나인지 검증
        assertEquals(1, successList.size(), "성공한 결제는 한 번이어야 합니다.");
        // 나머지 결제는 실패해야 함
        assertEquals(threadCount - 1, failList.size(), "나머지 결제 시도는 실패해야 합니다.");

        // 예약 상태가 완료되었는지 검증
        assertEquals(ReservationStatus.COMPLETE, latestReservation.getStatus(), "예약 상태가 완료되어야 합니다.");

    }

}
