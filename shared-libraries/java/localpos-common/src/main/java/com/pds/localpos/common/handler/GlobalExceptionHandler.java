package com.pds.localpos.common.handler;

import com.pds.localpos.common.exception.ApiException;
import com.pds.localpos.common.i18n.ErrorMessageResolver;
import com.pds.localpos.common.model.InternalApplicationError;
import com.pds.localpos.common.util.CorrelationIdHolder;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMessageResolver errorMessageResolver;
    private final Locale defaultLocale;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        String correlationId = CorrelationIdHolder.get();
        String message = errorMessageResolver.resolveMessage(ex.getMessage(), defaultLocale, ex.getMessageParams());

        log.warn("API Exception [{}]: {} - {}", correlationId, ex.getStatus(), message, ex);

        return buildErrorResponse(ex.getStatus(), correlationId, message);
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

        log.warn("Validation failed [{}]: {}", correlationId, message, ex);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, correlationId, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        String correlationId = CorrelationIdHolder.get();
        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> errorMessageResolver.resolveMessage(violation.getMessage(), defaultLocale))
                .findFirst()
                .orElse("Validation error");

        log.warn("Constraint violation [{}]: {}", correlationId, message, ex);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, correlationId, message);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        String correlationId = CorrelationIdHolder.get();
        String message = "You do not have permission to perform this action.";

        log.warn("Authorization denied [{}]: {}", correlationId, ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.FORBIDDEN, correlationId, message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        String correlationId = CorrelationIdHolder.get();

        log.warn("Bad credentials [{}]: {}", correlationId, ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.UNAUTHORIZED, correlationId, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        String correlationId = CorrelationIdHolder.get();
        String message = "Unexpected error: " + ex.getMessage();

        log.error("Unhandled exception [{}]: {}", correlationId, ex.getMessage(), ex);

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, correlationId, message);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String correlationId, String message) {
        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        InternalApplicationError error = InternalApplicationError.builder()
                .correlationId(correlationId)
                .timestamp(timestamp)
                .errorCode(status.name())
                .message(message)
                .build();

        return new ResponseEntity<>(error, status);
    }
}
