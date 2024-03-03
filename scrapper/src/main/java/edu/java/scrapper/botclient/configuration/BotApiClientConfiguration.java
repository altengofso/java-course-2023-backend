package edu.java.scrapper.botclient.configuration;

import edu.java.scrapper.app.configuration.ApplicationConfig;
import edu.java.scrapper.botclient.BotApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotApiClientConfiguration {
    @Bean
    public BotApiClient getBotApiClient(ApplicationConfig applicationConfig) {
        return new BotApiClient(applicationConfig.clientBaseUrl().botUrl());
    }
}
