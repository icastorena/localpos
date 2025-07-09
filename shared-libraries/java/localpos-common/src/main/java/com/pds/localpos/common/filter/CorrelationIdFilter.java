package com.pds.localpos.common.filter;

import com.pds.localpos.common.util.CorrelationIdHolder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class CorrelationIdFilter implements Filter {

    private static final String HEADER_NAME = "X-Correlation-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String correlationId = Optional.ofNullable(((HttpServletRequest) request).getHeader(HEADER_NAME))
                    .filter(id -> !id.isBlank())
                    .orElse(UUID.randomUUID().toString());

            CorrelationIdHolder.set(correlationId);
            MDC.put("correlationId", correlationId);
            chain.doFilter(request, response);
        } finally {
            CorrelationIdHolder.clear();
            MDC.clear();
        }
    }
}
