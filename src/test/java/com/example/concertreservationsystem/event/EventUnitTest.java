package com.example.concertreservationsystem.event;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.service.ConcertEventService;
import com.example.concertreservationsystem.infrastructure.config.SeatInitializer;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import com.example.concertreservationsystem.web.dto.event.request.EventRequestDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EventUnitTest {

    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private JpaConcertEventRepository concertEventRepository;
    @Mock
    private SeatInitializer seatInitializer;

    @InjectMocks
    private ConcertEventService concertEventService;

    @DisplayName("정상적으로 콘서트 이벤트가 생성되는지 테스트")
    @Test
    void 정상적으로_이벤트가_생성되는지_테스트() {

        Long eventId = 1L;
        Long availableSeats = 50L;
        Long totalSeats = 50L;
        LocalDate eventDate = LocalDate.now();
        String concertName = "ive";

        Concert concert = Concert.builder()
                .name(concertName)
                .price(1000L)
                .build();

        ConcertEvent event = ConcertEvent.builder()
                .eventDate(eventDate)
                .totalSeats(totalSeats)
                .concert(concert)
                .build();

         EventRequestDto requestDto = new EventRequestDto();
         requestDto.setConcertName(concertName);
         requestDto.setEventDate(eventDate);
         requestDto.setTotalSeats(totalSeats);


        when(concertRepository.findByName(concertName))
                .thenReturn(Optional.of(concert));
        when(concertEventRepository.existsByConcertAndEventDate(concert,eventDate))
                .thenReturn(false);

        EventResponseDto responseDto = concertEventService.registerConcertEvent(requestDto);
        assertEquals(requestDto.getTotalSeats(),responseDto.getTotalSeats());
        assertEquals(requestDto.getEventDate(),responseDto.getEventDate());

        verify(concertEventRepository).save(any(ConcertEvent.class));
    }


    @DisplayName("같은 날짜에 동일한 이름을 가진 콘서트 생성 불가한지 테스트")
    @Test
    void 같은_날짜에_동일한_이름을_가진_콘서트_생성_불가한지_테스트() {

        Long totalSeats = 50L;
        LocalDate eventDate = LocalDate.now();
        String concertName = "ive";

        Concert concert = Concert.builder()
                .name(concertName)
                .price(1000L)
                .build();

        EventRequestDto requestDto = new EventRequestDto();
        requestDto.setConcertName(concertName);
        requestDto.setEventDate(eventDate);
        requestDto.setTotalSeats(totalSeats);

        when(concertRepository.findByName(concertName))
                .thenReturn(Optional.of(concert));
        when(concertEventRepository.existsByConcertAndEventDate(concert,eventDate))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> concertEventService.registerConcertEvent(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 날짜에 콘서트 이벤트가 존재합니다.");
    }

}
