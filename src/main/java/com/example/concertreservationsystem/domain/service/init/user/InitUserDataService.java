package com.example.concertreservationsystem.domain.service.init.user;

import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class InitUserDataService {

    private final UserRepository userRepository;

    @Transactional
    public void generateUsers(int numberOfUsers) {
        for(int i=0; i<=numberOfUsers; i++) {
            User user = User.builder()
                    .name("testUser "+ i)
                    .build();
            userRepository.save(user);
        }
    }
}
