package com.example.concertreservationsystem.web.controller.payment;

import com.example.concertreservationsystem.application.usecase.PaymentUseCase;
import com.example.concertreservationsystem.web.dto.user.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제 API", description = "콘서트 결제 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    @Operation(summary = "콘서트 결제" ,
            description =
                    """
                    - 유저가 대기열 토큰으로 콘서트를 결제
                    """)
    @PostMapping
    public ResponseEntity<UserPaymentResponseDto> paymentConcert(@RequestParam String token,
                                                                 @RequestBody UserPaymentRequestDto requestDto) {
        UserPaymentResponseDto responseDto = paymentUseCase.paymentConcert(token, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
