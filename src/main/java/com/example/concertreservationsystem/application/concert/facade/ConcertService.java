package com.example.concertreservationsystem.application.concert.facade;

import com.example.concertreservationsystem.application.usecase.ConcertUseCase;
import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.application.concert.dto.request.ConcertRequestDto;
import com.example.concertreservationsystem.application.concert.dto.response.ConcertResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService implements ConcertUseCase{

    private final ConcertRepository concertRepository;


    @Override
    @Transactional
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto) {

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
}
