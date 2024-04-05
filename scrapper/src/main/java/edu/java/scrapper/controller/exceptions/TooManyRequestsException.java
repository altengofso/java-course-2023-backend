package edu.java.scrapper.controller.exceptions;

import edu.java.scrapper.controller.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TooManyRequestsException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ApiErrorResponse apiErrorResponse;

    public TooManyRequestsException(String message) {
        httpStatus = HttpStatus.TOO_MANY_REQUESTS;
        apiErrorResponse = new ApiErrorResponse(
            "Слишком много запросов",
            httpStatus.toString(),
            getClass().getSimpleName(),
            message,
            Arrays.stream(getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
