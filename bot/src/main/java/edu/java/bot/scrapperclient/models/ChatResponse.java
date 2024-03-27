package edu.java.bot.scrapperclient.models;

import java.time.OffsetDateTime;

public record ChatResponse(
    long id,
    OffsetDateTime createdAt
) {
}
