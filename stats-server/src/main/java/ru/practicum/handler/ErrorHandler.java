package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ApiError;
import ru.practicum.model.BadRequestException;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequest(final BadRequestException e) {
        log.warn(e.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Bad request", e.getMessage(), LocalDateTime.now()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
