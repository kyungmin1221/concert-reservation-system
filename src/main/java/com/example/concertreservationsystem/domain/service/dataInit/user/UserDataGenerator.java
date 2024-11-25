package com.example.concertreservationsystem.domain.service.dataInit.user;

import com.example.concertreservationsystem.domain.model.User;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import jakarta.persistence.EntityManager;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDataGenerator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void generateUsers(int count) {
        Faker faker = new Faker();
        int batchSize = 1000; // 배치 크기 조정
        List<User> users = new ArrayList<>(batchSize);

        for (int i = 1; i <= count; i++) {
            User user = User.builder()
                    .name(faker.name().fullName())
                    .build();

            users.add(user);

            if (i % batchSize == 0 || i == count) {
                userRepository.saveAll(users);
                entityManager.flush();
                entityManager.clear();
                users.clear();
                System.out.println(i + " 유저 저장");
            }
        }
    }
}
