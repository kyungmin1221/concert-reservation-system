package com.example.concertreservationsystem.web.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_TOKEN(BAD_REQUEST, "토큰이 유효하지 않습니다"),
    MISMATCH_TOKEN(BAD_REQUEST, "토큰의 유저 정보가 일치하지 않습니다"),
    WRONG_USERNAME(BAD_REQUEST, "유저의 이름이 잘못 입력되었습니다."),
    WRONG_RESERVATION_STATUS(BAD_REQUEST, "예약 대기 상태가 아닙니다.."),
    WRONG_DTO(BAD_REQUEST,"DTO를 다시 확인해주세요"),
    MISMATCH_ID(BAD_REQUEST,"잘못된 요청입니다."),

    // 401 UNAUTHORIZED: 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "권한 정보가 없는 회원입니다."),
    EXPIRED_QUEUE_TOKEN(UNAUTHORIZED,"만료된 대기열 TOKEN 입니다." ),


    // 403 FORBIDDEN : 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않다
    FORBIDDEN_CONCERT(FORBIDDEN,"본인이 예약한 콘서트가 아닙니다."),
    FORBIDDEN_SEAT(FORBIDDEN, "본인이 예약한 좌석이 아닙니다."),

    // 404 NOT_FOUND: 잘못된 리소스 접근
    CONCERT_NOT_FOUND(NOT_FOUND, "해당 콘서트 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(NOT_FOUND, "해당 예약 정보를 찾을 수 없습니다."),
    SEAT_NOT_FOUND(NOT_FOUND, "해당 좌석 정보를 찾을 수 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // 409 CONFLICT: 중복된 리소스 (요청이 현재 서버 상태와 충돌될 때)
    DUPLICATE_USER(CONFLICT, "이미 존재하는 유저입니다."),
    DUPLICATE_CONCERT(CONFLICT, "이미 존재하는 콘서트입니다."),
    DUPLICATE_CONCERT_EVENT(CONFLICT, "같은 날에 이미 존재하는 콘서트 이벤트입니다."),
    DUPLICATE_SEAT(CONFLICT, "예약 되어있는 좌석입니다."),
    DUPLICATE_QUEUE(CONFLICT, "이미 등록된 대기열입니다."),

    // 500 SERVER ERROR
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
