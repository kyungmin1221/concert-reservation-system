package com.example.concertreservationsystem.application.payment.facade;

import com.example.concertreservationsystem.application.payment.dto.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.application.payment.dto.response.UserPaymentResponseDto;

public interface PaymentFacade {
    public UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto);
}
