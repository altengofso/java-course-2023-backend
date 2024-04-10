package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.ApiErrorResponse;
import edu.java.scrapper.controller.exceptions.BadRequestException;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.controller.exceptions.TooManyRequestsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ApiErrorResponse> handleTooManyRequestsException(TooManyRequestsException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getApiErrorResponse());
    }
}
