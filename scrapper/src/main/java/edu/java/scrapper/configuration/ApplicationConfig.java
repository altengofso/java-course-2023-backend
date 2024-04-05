package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    ClientBaseUrl clientBaseUrl,
    AccessType databaseAccessType,
    RetryPolicy retryPolicy,
    RateLimit rateLimit
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration checkDelay) {
    }

    public record ClientBaseUrl(String githubUrl, String stackoverflowUrl, String botUrl) {
    }

    public enum AccessType {
        JDBC, JPA
    }

    public record RetryPolicy(
        Type type,
        Duration delay,
        int maxAttempts,
        Set<Integer> retryOnCodes

    ) {
    }

    public enum Type {
        NONE, CONSTANT, LINEAR, EXPONENTIAL
    }

    public record RateLimit(
        boolean enabled,
        int capacity,
        int refillRate,
        Duration refillInterval,
        int initialTokens
    ) {
    }
}
