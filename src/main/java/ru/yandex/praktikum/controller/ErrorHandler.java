package ru.yandex.praktikum.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(final Throwable e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
