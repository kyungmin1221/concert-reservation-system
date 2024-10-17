package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.QueueRepository;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaQueueRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaUserRepository;
import com.example.concertreservationsystem.web.dto.queue.response.QueueResponseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QueueUseCase {

    private final UserRepository userRepository;
    private final JpaQueueRepository queueRepository;

    @Transactional
    public QueueResponseToken addQueueToUser(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        // 유저가 있을 경우
        if(queueRepository.existsByUser(user)) {
            throw new IllegalArgumentException("이미 유저가 대기열에 진입하였습니다. ");
        }

        String token = generateQueueToken();
        Long position = queueRepository.count() + 1;

        QueueEntry queueEntry = QueueEntry.builder()
                .queueToken(token)
                .queuePosition(position)
                .joinQueue(LocalDateTime.now())
                .user(user)
                .build();

        queueRepository.save(queueEntry);

        return new QueueResponseToken(token, position);
    }


    // 대기열 토큰을 발급해주는 메서드
    private String generateQueueToken() {
        return UUID.randomUUID().toString();
    }
}
