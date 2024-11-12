package com.example.concertreservationsystem.application.payment.facade;

import com.example.concertreservationsystem.application.payment.dto.request.UserPaymentRequestDto;
import com.example.concertreservationsystem.application.payment.dto.response.UserPaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacadeImpl implements PaymentFacade{

    private final PaymentService paymentService;

    public UserPaymentResponseDto paymentConcert(String token, UserPaymentRequestDto requestDto){
        return paymentService.paymentConcert(token, requestDto);
    }
}
