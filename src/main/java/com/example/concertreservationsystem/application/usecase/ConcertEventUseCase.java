package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.repo.ConcertEventRepository;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.infrastructure.config.SeatInitializer;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import com.example.concertreservationsystem.web.dto.event.request.EventRequestDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ConcertEventUseCase {

    private final JpaConcertEventRepository concertEventRepository;
    private final ConcertRepository concertRepository;
    private final SeatInitializer seatInitializer;

    @Transactional
    public EventResponseDto registerConcertEvent(EventRequestDto requestDto) {

        Concert concert = concertRepository.findByName(requestDto.getConcertName())
                .orElseThrow(() -> new IllegalArgumentException("해당 콘서트를 찾을 수 없습니다."));

        LocalDate eventDate = requestDto.getEventDate();
        if (concertEventRepository.existsByConcertAndEventDate(concert, eventDate)) {
            throw new IllegalArgumentException("이미 해당 날짜에 콘서트 이벤트가 존재합니다.");
        }

        ConcertEvent concertEvent = ConcertEvent.builder()
                .eventDate(eventDate)
                .totalSeats(requestDto.getTotalSeats())
                .concert(concert)
                .build();

        concertEventRepository.save(concertEvent);
        seatInitializer.initializeSeatsForEvent(concertEvent);

        return new EventResponseDto(
                concertEvent.getEventDate(),
                concertEvent.getTotalSeats());
    }
}

