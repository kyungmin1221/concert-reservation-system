package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import com.example.concertreservationsystem.web.dto.concert.response.ConcertResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConcertUseCase {

    private final ConcertRepository concertRepository;

    @Transactional
    public ConcertResponseDto registerConcert(ConcertRequestDto requestDto) {

        String concertName = requestDto.getName();
        if(concertRepository.findByName(concertName).isPresent()) {
            throw new IllegalArgumentException("중복된 이름을 가진 콘서트는 등록할 수 없습니다.");
        }
        Concert concert = Concert.builder()
                .name(concertName)
                .build();
        concertRepository.save(concert);

        return new ConcertResponseDto(concert.getName());
    }
}

