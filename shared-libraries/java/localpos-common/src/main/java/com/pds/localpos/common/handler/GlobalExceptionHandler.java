package com.pds.localpos.common.handler;

import com.pds.localpos.common.constants.ErrorCodes;
import com.pds.localpos.common.constants.MessageCodes;
import com.pds.localpos.common.exception.ApiException;
import com.pds.localpos.common.i18n.ErrorMessageResolver;
import com.pds.localpos.common.model.InternalApplicationError;
import com.pds.localpos.common.util.CorrelationIdHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMessageResolver errorMessageResolver;
    private final Locale defaultLocale;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        String correlationId = CorrelationIdHolder.get();
        String message = errorMessageResolver.resolveMessage(ex.getMessageCode(), defaultLocale, ex.getMessage());
        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(ex.getErrorCode())
                .message(message)
                .details(ex.getMessage())
                .build();

        HttpStatus status = switch (ex.getErrorCode()) {
            case ErrorCodes.NOT_FOUND -> HttpStatus.NOT_FOUND;
            case ErrorCodes.INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        String correlationId = CorrelationIdHolder.get();
        String message = errorMessageResolver.resolveMessage(MessageCodes.ERROR_INTERNAL_ERROR, defaultLocale, "Unexpected internal error");
        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(ErrorCodes.INTERNAL_ERROR)
                .message(message)
                .details(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
