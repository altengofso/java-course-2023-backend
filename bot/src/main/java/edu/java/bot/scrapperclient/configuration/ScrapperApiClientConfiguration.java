package edu.java.bot.scrapperclient.configuration;

import edu.java.bot.app.configuration.ApplicationConfig;
import edu.java.bot.scrapperclient.ScrapperApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrapperApiClientConfiguration {
    @Bean
    public ScrapperApiClient getScrapperApiClient(ApplicationConfig applicationConfig) {
        if (applicationConfig.clientBaseUrl().scrapperUrl().isEmpty()) {
            throw new IllegalArgumentException(
                "app.client-base-url.scrapper-url needs to be defined in application.yml file");
        }
        return new ScrapperApiClient(applicationConfig.clientBaseUrl().scrapperUrl());
    }
}
