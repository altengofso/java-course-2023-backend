package edu.java.scrapper.controller.exceptions;

import edu.java.scrapper.controller.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ConflictException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ApiErrorResponse apiErrorResponse;

    public ConflictException(String message) {
        httpStatus = HttpStatus.CONFLICT;
        apiErrorResponse = new ApiErrorResponse(
            "Запрошенный ресурс уже существует",
            httpStatus.toString(),
            getClass().getSimpleName(),
            message,
            Arrays.stream(getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
