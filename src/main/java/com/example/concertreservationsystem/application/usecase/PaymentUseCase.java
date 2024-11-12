package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.application.user.dto.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.application.user.dto.response.UserPaymentResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface PaymentUseCase {

    UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto);
}
