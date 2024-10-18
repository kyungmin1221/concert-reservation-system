package com.example.concertreservationsystem.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    WRONG_USERNAME(BAD_REQUEST, "아이디가 잘못 입력되었습니다."),
    WRONG_PASSWORD(UNAUTHORIZED, "비밀번호가 잘못 입력되었습니다."),
    MISMATCH_PASSWORD(BAD_REQUEST,"두 비밀번호가 일치하지 않습니다."),
    UNVERIFIED_EMAIL(BAD_REQUEST, "이메일 인증이 완료되지 않았습니다."),
    WRONG_DTO(BAD_REQUEST,"DTO를 다시 확인해주세요"),
    MISMATCH_ID(BAD_REQUEST,"잘못된 요청입니다."),

    // 401 UNAUTHORIZED: 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "권한 정보가 없는 회원입니다."),
    EXPIRED_JWT(UNAUTHORIZED,"만료된 TOKEN 입니다." ),


    // 403 FORBIDDEN : 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않다
    FORBIDDEN_CONCERT(FORBIDDEN,"본인이 예약한 콘서트가 아닙니다."),
    FORBIDDEN_SEAT(FORBIDDEN, "본인이 예약한 좌석이 아닙니다."),

    // 404 NOT_FOUND: 잘못된 리소스 접근
    TOKEN_NOT_FOUND(NOT_FOUND, "로그아웃 된 사용자입니다"),
    CONCERT_NOT_FOUND(NOT_FOUND, "해당 콘서트 정보를 찾을 수 없습니다."),
    SEAT_NOT_FOUND(NOT_FOUND, "해당 좌석 정보를 찾을 수 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    // 409 CONFLICT: 중복된 리소스 (요청이 현재 서버 상태와 충돌될 때)
    DUPLICATE_EMAIL(CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_LIKE_TRUE(CONFLICT, "이미 좋아요 되어있습니다."),
    DUPLICATE_LIKE_FALSE(CONFLICT, "이미 좋아요가 취소 되어있습니다."),
    DUPLICATE_BOOKMARK(CONFLICT, "이미 등록된 북마크 입니다."),
    DUPLICATE_CHATROOM(CONFLICT, "이미 존재하는 채팅방 입니다."),


    // 500 SERVER ERROR
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

