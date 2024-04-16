package edu.java.bot.app.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    ClientBaseUrl clientBaseUrl,
    Topic updatesTopic,
    Topic deadLetterQueueTopic
) {
    public record ClientBaseUrl(String scrapperUrl) {
    }

    public record Topic(String name, int replicas, int partitions) {
    }
}
