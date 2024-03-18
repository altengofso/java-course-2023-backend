package edu.java.scrapper.service;

import java.time.OffsetDateTime;

public interface LinkUpdaterService {
    int update(OffsetDateTime lastCheckAt);
}
