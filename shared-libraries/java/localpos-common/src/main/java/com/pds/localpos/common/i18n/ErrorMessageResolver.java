package com.pds.localpos.common.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ErrorMessageResolver {

    private final MessageSource messageSource;

    public String resolveMessage(String code, Locale locale, String defaultMessage) {
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (Exception e) {
            return defaultMessage;
        }
    }
}
