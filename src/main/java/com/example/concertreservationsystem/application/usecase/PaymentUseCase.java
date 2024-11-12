package com.example.concertreservationsystem.application.usecase;

import com.example.concertreservationsystem.application.payment.dto.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.application.payment.dto.response.UserPaymentResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface PaymentUseCase {

    UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto);
}
