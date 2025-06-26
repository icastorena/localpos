package com.pds.localpos.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties(prefix = "app")
class I18nConfigurationProperties {

    var locale: String? = null

    val resolvedLocale: Locale
        get() = if (!locale.isNullOrBlank()) Locale.forLanguageTag(locale) else Locale.getDefault()
}
