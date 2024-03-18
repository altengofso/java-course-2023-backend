package edu.java.scrapper.controller.exceptions;

import edu.java.scrapper.controller.dto.ApiErrorResponse;
import java.util.Arrays;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ApiErrorResponse apiErrorResponse;

    public NotFoundException(String message) {
        httpStatus = HttpStatus.NOT_FOUND;
        apiErrorResponse = new ApiErrorResponse(
            "Не найден запрошенный ресурс",
            httpStatus.toString(),
            getClass().getSimpleName(),
            message,
            Arrays.stream(getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
