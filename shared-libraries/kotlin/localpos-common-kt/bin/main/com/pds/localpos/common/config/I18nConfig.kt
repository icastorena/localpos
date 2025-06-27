package com.pds.localpos.common.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class I18nConfig {

    @Bean
    fun messageSource(): MessageSource {
        return ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:i18n/messages")
            setDefaultEncoding("UTF-8")
            setCacheSeconds(3600)
        }
    }
}
