package com.example.concertreservationsystem.infrastructure.config.filter;


import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final QueueTokenInterceptor queueTokenInterceptor;

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoggingFilter()); // 등록할 필터
        filterRegistrationBean.setOrder(1); // 필터 순서
        filterRegistrationBean.addUrlPatterns("/*"); // 필터 적용할 url 패턴

        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(queueTokenInterceptor)
                .addPathPatterns("/api/reservations/**","api/users/**","/api/payments/**")
                .excludePathPatterns("/api/users/signin");

    }
}
