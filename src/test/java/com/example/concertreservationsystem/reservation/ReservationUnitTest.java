package com.example.concertreservationsystem.reservation;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import com.example.concertreservationsystem.domain.model.*;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.domain.service.ReservationService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaReservationRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import com.example.concertreservationsystem.web.dto.reservation.request.ReservationRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private QueueRepository queueRepository;
    @Mock
    private JpaConcertRepository concertRepository;
    @Mock
    private JpaSeatRepository seatRepository;
    @Mock
    private JpaReservationRepository reservationRepository;
    @InjectMocks
    private ReservationService reservationService;


    @DisplayName("좌석이 예약이 되는지 테스트")
    @Test
    void 좌석이_예약이_되는지_테스트() {

        // given
        String token = "valid-token";
        Long concertId = 1L;
        String uuid = "test-uuid";
        String seatNumber = "A1";
        Long eventId = 1L;

        User user = User.builder()
                .name("kyungmin")
                .build();

        Concert concert = Concert.builder()
                .name("Concert")
                .build();

        Seat seat = mock(Seat.class);  // Seat를 mock으로 처리
        QueueEntry queueEntry = QueueEntry.builder()
                .queueToken(token)
                .user(user)
                .build();

        ReservationRequestDto requestDto = new ReservationRequestDto();
        requestDto.setUuid(uuid);
        requestDto.setConcertName("Concert");
        requestDto.setEventId(eventId);
        requestDto.setSeatNumber(seatNumber);


        // Mock 설정 - validateToken이 성공하도록 설정
        when(queueRepository.findByQueueToken(token)).thenReturn(Optional.of(queueEntry));  // 대기열 토큰이 있는 상황
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));
        when(seatRepository.findSeatForUpdate(seatNumber, eventId)).thenReturn(Optional.of(seat));
        when(seat.isAvailable()).thenReturn(true);  // 좌석이 사용 가능한 상태

        // when
        reservationService.rvConcertToUser(concertId, token, requestDto);

        // then
        verify(seat).reserve();  // seat.reserve()가 호출되었는지 확인
        assertEquals(true, seat.isAvailable());
    }




        @DisplayName("콘서트 예약이 정상적으로 처리되는지 테스트")
        @Test
        void 콘서트_정상_예약_테스트() {

            // given
            String token = "valid-token";
            Long concertId = 1L;
            String uuid = "test-uuid";
            String seatNumber = "A1";
            Long eventId = 1L;

            User user = User.builder()
                    .name("kyungmin")
                    .build();

            Concert concert = Concert.builder()
                    .name("Concert")
                    .build();

            Seat seat = mock(Seat.class);

            QueueEntry queueEntry = QueueEntry.builder()
                    .queueToken(token)
                    .user(user)
                    .build();

            ReservationRequestDto requestDto = new ReservationRequestDto();
            requestDto.setUuid(uuid);
            requestDto.setConcertName("Concert");
            requestDto.setEventId(eventId);
            requestDto.setSeatNumber(seatNumber);

            // Mock 설정
            when(queueRepository.findByQueueToken(token)).thenReturn(Optional.of(queueEntry));  // 토큰 유효성 확인
            when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
            when(concertRepository.findById(concertId)).thenReturn(Optional.of(concert));
            when(seatRepository.findSeatForUpdate(seatNumber, eventId)).thenReturn(Optional.of(seat));
            when(seat.isAvailable()).thenReturn(true);  // 좌석이 사용 가능한 상태

            // when
            reservationService.rvConcertToUser(concertId, token, requestDto);

            // then
            verify(reservationRepository).save(any(Reservation.class));  // 예약이 저장되었는지 확인
         }
}
