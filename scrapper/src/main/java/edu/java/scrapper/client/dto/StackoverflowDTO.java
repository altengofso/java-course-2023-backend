package edu.java.scrapper.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackoverflowDTO(List<Item> items) {
    public record Item(@JsonProperty("last_activity_date") OffsetDateTime updatedAt) {
    }
}
