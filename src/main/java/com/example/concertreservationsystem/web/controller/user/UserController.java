package com.example.concertreservationsystem.web.controller.user;

import com.example.concertreservationsystem.application.usecase.UserUseCase;
import com.example.concertreservationsystem.domain.repo.UserRepository;
import com.example.concertreservationsystem.web.dto.user.request.UserPointRequestDto;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPointResponseDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    /**
     * 유저 등록
     * @param requestDto
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody  UserRequestDto requestDto) {
        UserResponseDto responseDto = userUseCase.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 잔액 충전
     * @param token : 대기열 토큰
     * @param requestDto : 충전 금액
     * @return
     */
    @PostMapping("/charge")
    public ResponseEntity<UserPointResponseDto> chargePoint(@RequestParam String token,
                                                            @RequestBody UserPointRequestDto requestDto) {
        UserPointResponseDto responseDto = userUseCase.chargePoint(token, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 잔액 조회
     * @param token : 대기열 토큰
     * @return : 현재 남은 금액
     */
    @GetMapping("/chargePoint")
    public ResponseEntity<UserPointResponseDto> getUserPoint(@RequestParam String token) {
        UserPointResponseDto responseDto = userUseCase.getUserPoint(token);
        return ResponseEntity.ok(responseDto);
    }
}
