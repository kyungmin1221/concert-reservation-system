package com.example.concertreservationsystem.web.controller.user;

import com.example.concertreservationsystem.domain.service.UserService;
import com.example.concertreservationsystem.web.dto.user.request.UserRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 유저 등록
     * @param requestDto
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody  UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
