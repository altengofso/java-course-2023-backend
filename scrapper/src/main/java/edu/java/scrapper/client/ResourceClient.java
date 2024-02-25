package edu.java.scrapper.client;

import java.time.OffsetDateTime;

public interface ResourceClient {
    OffsetDateTime getUpdatedAt(String link);
}
