package edu.java.scrapper.app.repository.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkDto(long id, URI url, OffsetDateTime lastCheckedAt, OffsetDateTime updatedAt) {
}
