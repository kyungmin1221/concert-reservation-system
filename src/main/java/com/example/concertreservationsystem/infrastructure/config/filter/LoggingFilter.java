package com.example.concertreservationsystem.infrastructure.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("====== doFilter 실행 ===== ");
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        log.info(" '" + servletRequest.getRequestURI() + "' 라는 요청 URL 실행");
        log.info("Method Type : " + servletRequest.getMethod());

        chain.doFilter(request, response);

        log.info("Status : " + servletResponse.getStatus() + " 을 반환하였습니다. ");
    }
}
