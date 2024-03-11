package edu.java.scrapper.botclient.configuration;

import edu.java.scrapper.app.configuration.ApplicationConfig;
import edu.java.scrapper.botclient.BotApiClient;
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
