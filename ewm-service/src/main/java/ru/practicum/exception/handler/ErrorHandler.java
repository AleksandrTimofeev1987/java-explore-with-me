package ru.practicum.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.model.*;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Bad request", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleNotValidMethodArgument(final MethodArgumentNotValidException e) {
        log.warn(e.getFieldError().getDefaultMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Incorrect request", e.getFieldError().getDefaultMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Incorrect request", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleConflictException(final ConflictException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Conflict request", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleForbiddenException(final ForbiddenException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, "Forbidden request", e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "Entity not found", e.getMessage(), LocalDateTime.now()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
