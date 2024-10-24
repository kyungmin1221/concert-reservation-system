package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.web.dto.user.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.web.dto.user.response.UserPaymentResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface PaymentUseCase {

    UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto);
}
