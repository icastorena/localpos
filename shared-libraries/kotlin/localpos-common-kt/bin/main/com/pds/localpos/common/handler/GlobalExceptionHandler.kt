package com.pds.localpos.common.handler

import com.pds.localpos.common.exception.ApiException
import com.pds.localpos.common.i18n.ErrorMessageResolver
import com.pds.localpos.common.model.InternalApplicationError
import com.pds.localpos.common.util.CorrelationIdHolder
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RestControllerAdvice
class GlobalExceptionHandler(
    private val errorMessageResolver: ErrorMessageResolver,
    private val defaultLocale: Locale
) {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<Any> {
        val correlationId = CorrelationIdHolder.get()
        val message = errorMessageResolver.resolveMessage(ex.message ?: "", defaultLocale, *ex.messageParams)
        logger.warn("API Exception [{}]: {} - {}", correlationId, ex.status, message, ex)
        return buildErrorResponse(ex.status, correlationId, message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val correlationId = CorrelationIdHolder.get()
        val message = ex.bindingResult.fieldErrors
            .map { errorMessageResolver.resolveMessage(it.defaultMessage ?: "", defaultLocale) }
            .firstOrNull() ?: "Validation error"

        logger.warn("Validation failed [{}]: {}", correlationId, message, ex)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, correlationId, message)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<Any> {
        val correlationId = CorrelationIdHolder.get()
        val message = ex.constraintViolations
            .map { errorMessageResolver.resolveMessage(it.message, defaultLocale) }
            .firstOrNull() ?: "Validation error"

        logger.warn("Constraint violation [{}]: {}", correlationId, message, ex)
        return buildErrorResponse(HttpStatus.BAD_REQUEST, correlationId, message)
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(ex: AuthorizationDeniedException): ResponseEntity<Any> {
        val correlationId = CorrelationIdHolder.get()
        val message = "You do not have permission to perform this action."
        logger.warn("Authorization denied [{}]: {}", correlationId, ex.message, ex)
        return buildErrorResponse(HttpStatus.FORBIDDEN, correlationId, message)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<Any> {
        val correlationId = CorrelationIdHolder.get()
        val message = ex.message ?: "Invalid credentials"
        logger.warn("Bad credentials [{}]: {}", correlationId, message, ex)
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, correlationId, message)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Any> {
        val correlationId = CorrelationIdHolder.get()
        val message = "Unexpected error: ${ex.message ?: "Unknown"}"
        logger.error("Unhandled exception [{}]: {}", correlationId, ex.message, ex)
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, correlationId, message)
    }

    private fun buildErrorResponse(
        status: HttpStatus,
        correlationId: String,
        message: String
    ): ResponseEntity<Any> {
        val timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())

        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = timestamp,
            errorCode = status.name,
            message = message
        )

        return ResponseEntity(error, status)
    }
}
