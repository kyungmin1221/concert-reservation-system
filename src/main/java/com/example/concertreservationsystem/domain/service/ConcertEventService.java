package com.example.concertreservationsystem.domain.service;


import com.example.concertreservationsystem.application.usecase.ConcertEventUseCase;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.ConcertEvent;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.infrastructure.config.SeatInitializer;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertEventRepository;
import com.example.concertreservationsystem.web.dto.event.request.EventRequestDto;
import com.example.concertreservationsystem.web.dto.event.response.EventResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertEventService implements ConcertEventUseCase{

    private final ConcertRepository concertRepository;
    private final JpaConcertEventRepository concertEventRepository;
    private final SeatInitializer seatInitializer;

    @Override
    @Transactional
    public EventResponseDto registerConcertEvent(EventRequestDto requestDto) {

        Concert concert = concertRepository.findByName(requestDto.getConcertName())
                .orElseThrow(() -> new IllegalArgumentException("해당 콘서트를 찾을 수 없습니다."));

        LocalDate eventDate = requestDto.getEventDate();
        if (concertEventRepository.existsByConcertAndEventDate(concert, eventDate)) {
            log.error("{} 에 이미 콘서트가 있다.= ",eventDate);
            log.error("콘서트 이름 ={}", concert.getName());
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
