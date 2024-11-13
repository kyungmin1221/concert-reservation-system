package com.example.concertreservationsystem.web.exception;


import com.example.concertreservationsystem.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 이미 데이터가 존재하는지 확인하여 최초 한 번만 실행되도록 합니다.
        if (userService.countUsers() == 0) {
            userService.generateDummyUsers(100000);
        }
    }
}
