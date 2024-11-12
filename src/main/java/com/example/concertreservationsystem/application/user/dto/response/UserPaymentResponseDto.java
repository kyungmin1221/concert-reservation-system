package com.example.concertreservationsystem.application.user.dto.response;

import com.example.concertreservationsystem.domain.constant.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPaymentResponseDto {

    private Long currentPoint;
    private String reservationStatus;   // 예약 상태
    private String message;

    public UserPaymentResponseDto(Long point, ReservationStatus status, String message) {
        this.currentPoint = point;
        this.reservationStatus = status.toString();
        this.message = message;
    }
}
