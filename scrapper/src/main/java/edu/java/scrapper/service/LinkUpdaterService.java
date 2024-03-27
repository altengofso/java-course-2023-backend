package edu.java.scrapper.service;

import java.time.OffsetDateTime;

public interface LinkUpdaterService {
    void update(OffsetDateTime lastCheckAt);
}
