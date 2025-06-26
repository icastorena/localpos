package com.pds.localpos.userservice.config

import com.pds.localpos.common.handler.GlobalExceptionHandler
import com.pds.localpos.common.i18n.ErrorMessageResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExceptionHandlerConfig(
    private val errorMessageResolver: ErrorMessageResolver,
    private val i18nConfigurationProperties: I18nConfigurationProperties
) {

    @Bean
    fun globalExceptionHandler(): GlobalExceptionHandler {
        return GlobalExceptionHandler(
            errorMessageResolver,
            i18nConfigurationProperties.resolvedLocale
        )
    }
}

