package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.ConcertUseCase;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import com.example.concertreservationsystem.web.dto.concert.response.ConcertResponseDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService implements ConcertUseCase{

    private final ConcertRepository concertRepository;


    @Override
    @Transactional
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto) {

        String concertName = requestDto.getName();
        if(concertRepository.findByName(concertName).isPresent()) {
            throw new IllegalArgumentException("중복된 이름을 가진 콘서트는 등록할 수 없습니다.");
        }
        Concert concert = Concert.builder()
                .name(concertName)
                .price(requestDto.getPrice())
                .build();
        concertRepository.save(concert);

        return new ConcertResponseDto(concert.getName(), concert.getPrice());
    }
}
