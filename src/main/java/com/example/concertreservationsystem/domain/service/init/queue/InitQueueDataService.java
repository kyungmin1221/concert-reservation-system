package com.example.concertreservationsystem.domain.service.init.queue;

import com.example.concertreservationsystem.domain.model.QueueEntry;
import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.infrastructure.persistence.JpaQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InitQueueDataService {

    private final UserRepository userRepository;
    private final JpaQueueRepository queueRepository;

    @Transactional
    public void generateQueueEntries(int numberOfEntries) {
        List<User> users = userRepository.findAll();
        for (int i = 0; i < numberOfEntries; i++) {
            User user = users.get(i % users.size());
            String token = UUID.randomUUID().toString();
            QueueEntry queueEntry = QueueEntry.builder()
                    .queueToken(token)
                    .joinQueue(LocalDateTime.now().plusDays(i))
                    .queuePosition((long)i)
                    .user(user)
                    .build();
            queueRepository.save(queueEntry);
        }
    }
}
