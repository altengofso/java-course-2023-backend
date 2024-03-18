package edu.java.scrapper.controller.exceptions;

import edu.java.scrapper.controller.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ApiErrorResponse apiErrorResponse;

    public BadRequestException(String message) {
        httpStatus = HttpStatus.BAD_REQUEST;
        apiErrorResponse = new ApiErrorResponse(
            "Некорректные параметры запроса",
            httpStatus.toString(),
            getClass().getSimpleName(),
            message,
            Arrays.stream(getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
