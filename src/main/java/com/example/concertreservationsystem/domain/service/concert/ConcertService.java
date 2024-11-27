package com.example.concertreservationsystem.domain.service.concert;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.model.Seat;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.application.concert.dto.request.ConcertRequestDto;
import com.example.concertreservationsystem.application.concert.dto.response.ConcertResponseDto;
import com.example.concertreservationsystem.infrastructure.persistence.JpaConcertRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService{

    private final ConcertRepository concertRepository;

    @Transactional
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto) {
        log.info("콘서트를 등록합니다.");
        String concertName = requestDto.getName();
        if(concertRepository.findByName(concertName).isPresent()) {
            log.error("이미 존재하는 이름을 가진 콘서트가 있다. = {}",concertName);
            throw new IllegalArgumentException("중복된 이름을 가진 콘서트는 등록할 수 없습니다.");
        }
        Concert concert = Concert.builder()
                .name(concertName)
                .price(requestDto.getPrice())
                .build();
        concertRepository.save(concert);

        return new ConcertResponseDto(concert.getName(), concert.getPrice());
    }

    public Concert getConcertById(Long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 콘서트가 없습니다."));
    }

    public Concert getConcertByName(String concertName) {
        return concertRepository.findByName(concertName)
                .orElseThrow(() -> new IllegalArgumentException("등록된 콘서트 이름이 없습니다."));
    }

}
