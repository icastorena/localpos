package com.pds.localpos.common.i18n

import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class ErrorMessageResolver(private val messageSource: MessageSource) {

    fun resolveMessage(code: String, locale: Locale, vararg args: Any): String {
        return try {
            messageSource.getMessage(code, args, locale)
        } catch (ex: Exception) {
            "Message resolve error"
        }
    }
}
