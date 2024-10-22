package com.example.concertreservationsystem.concert;

import com.example.concertreservationsystem.domain.model.Concert;
import com.example.concertreservationsystem.domain.repo.ConcertRepository;
import com.example.concertreservationsystem.domain.service.ConcertService;
import com.example.concertreservationsystem.web.dto.concert.request.ConcertRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConcertUnitTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;

    @DisplayName("콘서트 중복 생성 예외 테스트")
    @Test
    void 콘서트_중복_생성_예외() {

        // given
        String concertName = "ive";
        Long concertPrice = 1000L;

        ConcertRequestDto requestDto = new ConcertRequestDto();
        requestDto.setName(concertName);
        requestDto.setPrice(concertPrice);

        when(concertRepository.findByName(concertName))
                .thenReturn(Optional.of(new Concert()));

        // when & then
        assertThatThrownBy(() -> concertService.registerConcert(requestDto))
                .isInstanceOf(IllegalArgumentException.class)  // 발생할 예외 타입 검증
                .hasMessage("중복된 이름을 가진 콘서트는 등록할 수 없습니다.");  // 예외 메시지 검증
    }

}
