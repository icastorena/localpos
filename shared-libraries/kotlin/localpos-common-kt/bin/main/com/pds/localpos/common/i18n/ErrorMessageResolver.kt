package com.pds.localpos.common.i18n

import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class ErrorMessageResolver(private val messageSource: MessageSource) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun resolveMessage(code: String, locale: Locale, vararg args: Any): String {
        return try {
            messageSource.getMessage(code, args, locale)
        } catch (ex: Exception) {
            logger.warn(
                "Unable to resolve message for code '{}', locale '{}', args: {}, error: {}",
                code,
                locale,
                args.contentToString(),
                ex.toString()
            )
            "Message resolve error"
        }
    }
}
