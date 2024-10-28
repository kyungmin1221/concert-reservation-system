package com.example.concertreservationsystem.domain.service;

import com.example.concertreservationsystem.application.usecase.QueueUseCase;
import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaQueueRepository;
import com.example.concertreservationsystem.web.dto.queue.response.QueueResponseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService implements QueueUseCase{

    private final UserRepository userRepository;
    private final JpaQueueRepository queueRepository;

    @Override
    @Transactional
    public QueueResponseToken addQueueToUser(String uuid) {

        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("등록된 유저가 없습니다."));

        // 유저가 있을 경우
        if(queueRepository.existsByUser(user)) {
            log.error("이미 유저가 대기열 진입한 상태 = {}", user.getName());
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
