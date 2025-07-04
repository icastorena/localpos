package com.pds.localpos.productservice.config;

import com.pds.localpos.common.handler.GlobalExceptionHandler;
import com.pds.localpos.common.i18n.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class ExceptionHandlerConfig {

    private final ErrorMessageResolver errorMessageResolver;
    private final I18nConfigurationProperties i18nConfigurationProperties;

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        Locale locale = i18nConfigurationProperties.getLocale();
        return new GlobalExceptionHandler(errorMessageResolver, locale);
    }
}
