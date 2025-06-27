package com.pds.localpos.common.handler

import com.pds.localpos.common.exception.ApiException
import com.pds.localpos.common.i18n.ErrorMessageResolver
import com.pds.localpos.common.model.InternalApplicationError
import com.pds.localpos.common.util.CorrelationIdHolder
import jakarta.validation.ConstraintViolationException
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

    private fun currentTimestamp(): String =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<InternalApplicationError> {
        val correlationId = CorrelationIdHolder.get()
        val message = errorMessageResolver.resolveMessage(ex.message ?: "", defaultLocale, *ex.messageParams)
        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = currentTimestamp(),
            errorCode = ex.status.name,
            message = message
        )
        return ResponseEntity(error, ex.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<InternalApplicationError> {
        val correlationId = CorrelationIdHolder.get()
        val message = ex.bindingResult.fieldErrors
            .map { errorMessageResolver.resolveMessage(it.defaultMessage ?: "", defaultLocale) }
            .firstOrNull() ?: "Validation error"
        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = currentTimestamp(),
            errorCode = HttpStatus.BAD_REQUEST.name,
            message = message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<InternalApplicationError> {
        val correlationId = CorrelationIdHolder.get()
        val message = ex.constraintViolations
            .map { errorMessageResolver.resolveMessage(it.message, defaultLocale) }
            .firstOrNull() ?: "Validation error"
        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = currentTimestamp(),
            errorCode = HttpStatus.BAD_REQUEST.name,
            message = message
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(ex: AuthorizationDeniedException): ResponseEntity<InternalApplicationError> {
        val correlationId = CorrelationIdHolder.get()
        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = currentTimestamp(),
            errorCode = HttpStatus.FORBIDDEN.name,
            message = "You do not have permission to perform this action."
        )
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<InternalApplicationError> {
        val correlationId = CorrelationIdHolder.get()
        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = currentTimestamp(),
            errorCode = HttpStatus.UNAUTHORIZED.name,
            message = ex.message ?: "Invalid credentials"
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<InternalApplicationError> {
        val correlationId = CorrelationIdHolder.get()
        val error = InternalApplicationError(
            correlationId = correlationId,
            timestamp = currentTimestamp(),
            errorCode = HttpStatus.INTERNAL_SERVER_ERROR.name,
            message = ex.message ?: "Unexpected error"
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
