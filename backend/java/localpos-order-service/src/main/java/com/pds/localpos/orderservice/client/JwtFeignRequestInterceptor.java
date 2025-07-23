package com.pds.localpos.orderservice.client;

import com.pds.localpos.security.util.JwtTokenProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFeignRequestInterceptor implements RequestInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void apply(RequestTemplate template) {
        String token = jwtTokenProvider.getToken();

        if (token != null && !token.isBlank()) {
            template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
    }
}

