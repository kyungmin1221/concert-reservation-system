package com.example.concertreservationsystem.user;

import com.example.concertreservationsystem.domain.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class UserUnitTest {

    @DisplayName("유저의 잔액 충전")
    @Test
    void 유저의_잔액_충전() {
        // given
        Long userId = 1L;
        String username = "kyungmin";
        String uuid = "abc";

        User user = User.builder()
                .id(userId)
                .name(username)
                .build();
        // when
        user.addPoints(1000L);

        // then
        assertThat(user.getPoint()).isEqualTo(1000L);
    }

    @DisplayName("유저의 콘서트 결제 시 잔액 부족 -> 예외")
    @Test
    void 유저의_결제_잔액부족_오류() {
        // given
        Long userId = 1L;
        String username = "kyungmin";
        String uuid = "abc";
        Long concertPrice = 1000L;

        // 유저의 잔액이 500이라고 가정
        User user = User.builder()
                .id(userId)
                .name(username)
                .build();

        // when & then
        assertThatThrownBy(() -> user.minusPoints(concertPrice))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("콘서트를 결제할 잔액이 부족합니다. 금액을 충전하세요");

    }
}