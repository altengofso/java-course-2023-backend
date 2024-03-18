package edu.java.scrapper.repository.dto;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkDto(long id, URI url, OffsetDateTime lastCheckAt, OffsetDateTime updatedAt) {
}
