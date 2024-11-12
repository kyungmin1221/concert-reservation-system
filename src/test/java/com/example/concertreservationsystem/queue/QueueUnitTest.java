package com.example.concertreservationsystem.queue;

import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.application.queue.facade.QueueService;
import com.example.concertreservationsystem.infrastructure.persistence.JpaQueueRepository;
import com.example.concertreservationsystem.application.queue.dto.QueueResponseToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class QueueUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JpaQueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;


    @DisplayName("정상적으로 대기열 토큰이 발급되는지 테스트")
    @Test
    void 정상적으로_토큰이_발급되는지_테스트() {

        // given
        Long userId = 1L;
        String username = "testuser";
        String userUuid = "test-uuid";
        User user = User.builder()
                .id(userId)
                .name(username)
                .build();

        when(userRepository.findByUuid(userUuid))
                .thenReturn(Optional.of(user));
        when(queueRepository.existsByUser(user))
                .thenReturn(false);
        when(queueRepository.count()).thenReturn(0L);

        // when
        QueueResponseToken responseToken = queueService.addQueueToUser(userUuid);

        // then
        assertThat(responseToken.getQueueToken()).isNotNull();
        assertThat(responseToken.getQueuePosition()).isEqualTo(1L); // 대기열 정상 진입
    }

    @DisplayName("유저가 대기열에 없을 경우 정상적으로 대기열에 진입하는지 테스트")
    @Test
    void 유저가_대기열에_없을_경우_정상적으로_대기열에_진입하는지_테스트() {

        // given
        Long userId = 1L;
        String username = "testuser";
        String userUuid = "test-uuid";
        User user = User.builder()
                .id(userId)
                .name(username)
                .build();

        when(userRepository.findByUuid(userUuid))
                .thenReturn(Optional.of(user));
        when(queueRepository.existsByUser(user))
                .thenReturn(false);
        when(queueRepository.count()).thenReturn(5L);

        // when
        QueueResponseToken responseToken = queueService.addQueueToUser(userUuid);

        // then
        assertThat(responseToken.getQueuePosition()).isEqualTo(6L);  // 6번째 대기열


    }

    @DisplayName("유저가 대기열에 이미 있을 경우 예외 발생 테스트")
    @Test
    void 유저가_대기열에_이미_있을_경우_예외_발생_테스트(){

        // given
        Long userId = 1L;
        String username = "testuser";
        String userUuid = "test-uuid";
        User user = User.builder()
                .id(userId)
                .name(username)
                .build();

        when(userRepository.findByUuid(userUuid))
                .thenReturn(Optional.of(user));
        when(queueRepository.existsByUser(user))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> queueService.addQueueToUser(userUuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 유저가 대기열에 진입하였습니다. ");
    }
}
