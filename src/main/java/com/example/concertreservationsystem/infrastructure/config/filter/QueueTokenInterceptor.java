package com.example.concertreservationsystem.infrastructure.config.filter;

import com.example.concertreservationsystem.domain.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class QueueTokenInterceptor implements HandlerInterceptor {

    private final ReservationService reservationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("======== 대기열 토큰 인터셉터 실행 ==========");

        String queueToken = request.getParameter("token");

        if (queueToken == null || !reservationService.isValidToken(queueToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 토큰이 유효하지 않으면 401 에러 반환
            return false;
        }

        log.info("======== 대기열 토큰 인터셉터 종료 ==========");
        return true;
    }
}
