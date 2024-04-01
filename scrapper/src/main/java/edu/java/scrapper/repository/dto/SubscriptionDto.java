package edu.java.scrapper.repository.dto;

import java.time.OffsetDateTime;

public record SubscriptionDto(long linkId, long chatId, OffsetDateTime createdAt) {
}
