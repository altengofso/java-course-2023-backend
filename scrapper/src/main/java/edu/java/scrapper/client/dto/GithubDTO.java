package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GithubDTO(@JsonProperty("updated_at") OffsetDateTime updatedAt) {
}
