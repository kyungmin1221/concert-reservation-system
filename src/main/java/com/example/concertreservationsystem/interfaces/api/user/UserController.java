package com.example.concertreservationsystem.interfaces.api.user;

import com.example.concertreservationsystem.application.user.dto.request.UserPointRequestDto;
import com.example.concertreservationsystem.application.user.dto.request.UserRequestDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPointResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPositionResponseDto;
import com.example.concertreservationsystem.application.user.dto.response.UserResponseDto;
import com.example.concertreservationsystem.application.user.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    /**
     * 유저 등록
     * @param requestDto
     * @return
     */
    @Operation(summary = "유저 생성" ,
            description =
                    """
                    - 유저 생성 ,
                    - 유저의 uuid 를 반환
                    """)
    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody  UserRequestDto requestDto) {
        UserResponseDto responseDto = userFacade.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 잔액 충전
     * @param token : 대기열 토큰
     * @param requestDto : 충전 금액
     * @return
     */
    @Operation(
            summary = "유저의 잔액 충전",
            description = "대기열 토큰으로 유저의 잔액을 충전합니다.",
            parameters = {
                    @Parameter(name = "token", description = "대기열 토큰", example = "abc-def")
            }
    )
    @PostMapping("/charge")
    public ResponseEntity<UserPointResponseDto> chargePoint(@RequestParam String token,
                                                            @RequestBody UserPointRequestDto requestDto) {
        UserPointResponseDto responseDto = userFacade.chargePoint(token, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 잔액 조회
     * @param token : 대기열 토큰
     * @return : 현재 남은 금액
     */
    @Operation(
            summary = "유저의 잔액 조회",
            description = "대기열 토큰으로 유저의 잔액을 조회합니다.",
            parameters = {
                    @Parameter(name = "token", description = "대기열 토큰", example = "abc-def")
            }
    )
    @GetMapping("/chargePoint")
    public ResponseEntity<UserPointResponseDto> getUserPoint(@RequestParam String token) {
        UserPointResponseDto responseDto = userFacade.getUserPoint(token);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/position")
    public ResponseEntity<UserPositionResponseDto> getUserQueuePosition(@RequestParam String token) {
        UserPositionResponseDto responseDto = userFacade.getUserQueuePosition(token);
        return ResponseEntity.ok(responseDto);
    }
}