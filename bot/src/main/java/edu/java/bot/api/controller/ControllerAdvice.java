package edu.java.bot.api.controller;

import edu.java.bot.api.exceptions.BadRequestException;
import edu.java.bot.api.models.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getApiErrorResponse());
    }
}
