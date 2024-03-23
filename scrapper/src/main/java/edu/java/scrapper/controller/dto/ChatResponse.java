package edu.java.scrapper.controller.dto;

import java.time.OffsetDateTime;

public record ChatResponse(
    long id,
    OffsetDateTime createdAt
) {
}
