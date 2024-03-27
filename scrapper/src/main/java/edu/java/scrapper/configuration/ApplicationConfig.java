package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    ClientBaseUrl clientBaseUrl,
    AccessType databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration checkDelay) {
    }

    public record ClientBaseUrl(String githubUrl, String stackoverflowUrl, String botUrl) {
    }

    public enum AccessType {
        JDBC, JPA
    }
}
