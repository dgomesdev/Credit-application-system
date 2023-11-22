package com.dgomesdev.creditapplicationsystem.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val errors: MutableMap<String, String?> = HashMap()
        exception.bindingResult.allErrors.stream().forEach { error ->
            val fieldName: String = (error as FieldError).field
            val errorMessage: String? = error.defaultMessage
            errors[fieldName] = errorMessage
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Conflict of data! Consult the documentation",
                timeStamp = LocalDateTime.now(),
                status = exception.statusCode.value(),
                exception = exception.localizedMessage,
                details = errors
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(exception: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ExceptionDetails(
                title = "Bad request! Consult the documentation",
                timeStamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                exception = exception.localizedMessage,
                details = mutableMapOf(exception.cause.toString() to exception.message)
            )
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionDetails(
                title = "Bad request! Consult the documentation",
                timeStamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = exception.localizedMessage,
                details = mutableMapOf(exception.cause.toString() to exception.message)
            )
        )
    }

@ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ExceptionDetails(
                title = "Unauthorized request! Contact admin",
                timeStamp = LocalDateTime.now(),
                status = HttpStatus.UNAUTHORIZED.value(),
                exception = exception.localizedMessage,
                details = mutableMapOf(exception.cause.toString() to exception.message)
            )
        )
    }
}