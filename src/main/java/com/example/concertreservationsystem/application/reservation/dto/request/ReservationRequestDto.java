package com.example.concertreservationsystem.application.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequestDto {

    // 예약을 할 콘서트 이름
    private String concertName;
    // 예약을 할 콘서트의 이벤트 id
    private Long eventId;
    // 예약을 할 콘서트 좌석 번호
    private String seatNumber;
}
