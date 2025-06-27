package com.pds.localpos.common.handler;

import com.pds.localpos.common.exception.ApiException;
import com.pds.localpos.common.i18n.ErrorMessageResolver;
import com.pds.localpos.common.model.InternalApplicationError;
import com.pds.localpos.common.util.CorrelationIdHolder;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

        String message = errorMessageResolver.resolveMessage(
                ex.getMessage(),
                defaultLocale,
                ex.getMessageParams()
        );

        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(ex.getStatus().name())
                .message(message)
                .build();

        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        String correlationId = CorrelationIdHolder.get();

        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String correlationId = CorrelationIdHolder.get();

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> errorMessageResolver.resolveMessage(fieldError.getDefaultMessage(), defaultLocale))
                .findFirst()
                .orElse("Validation error");

        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(HttpStatus.BAD_REQUEST.name())
                .message(message)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        String correlationId = CorrelationIdHolder.get();

        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> errorMessageResolver.resolveMessage(violation.getMessage(), defaultLocale))
                .findFirst()
                .orElse("Validation error");

        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(HttpStatus.BAD_REQUEST.name())
                .message(message)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        String correlationId = CorrelationIdHolder.get();
        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(HttpStatus.FORBIDDEN.name())
                .message("You do not have permission to perform this action.")
                .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        String correlationId = CorrelationIdHolder.get();

        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(HttpStatus.UNAUTHORIZED.name())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
