package com.pds.localpos.orderservice.config;

import com.pds.localpos.security.util.JwtTokenProvider;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor jwtRequestInterceptor(JwtTokenProvider jwtTokenProvider) {
        return template -> {
            String token = jwtTokenProvider.getToken();
            if (token != null && !token.isBlank()) {
                template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            }
        };
    }
}
