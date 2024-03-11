package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.exceptions.BadRequestException;
import edu.java.scrapper.api.exceptions.ConflictException;
import edu.java.scrapper.api.exceptions.NotFoundException;
import edu.java.scrapper.api.models.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice()
public class ContollerAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getApiErrorResponse());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getApiErrorResponse());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException(ConflictException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getApiErrorResponse());
    }
}
