package edu.java.scrapper.client.botclient.configuration;

import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotApiClientConfiguration {
    @Bean
    public BotApiClient getBotApiClient(ApplicationConfig applicationConfig) {
        if (applicationConfig.clientBaseUrl().botUrl().isEmpty()) {
            throw new IllegalArgumentException(
                "app.client-base-url.bot-url needs to be defined in application.yml file"
            );
        }
        return new BotApiClient(applicationConfig.clientBaseUrl().botUrl());
    }
}
