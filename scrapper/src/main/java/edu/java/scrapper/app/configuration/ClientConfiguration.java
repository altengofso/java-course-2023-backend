package edu.java.scrapper.app.configuration;

import edu.java.scrapper.app.client.GithubApiClient;
import edu.java.scrapper.app.client.StackoverflowApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubApiClient getGithubClient(ApplicationConfig applicationConfig) {
        return new GithubApiClient(applicationConfig.clientBaseUrl().githubUrl());
    }

    @Bean
    public StackoverflowApiClient getStackoverflowClient(ApplicationConfig applicationConfig) {
        return new StackoverflowApiClient(applicationConfig.clientBaseUrl().stackoverflowUrl());
    }
}
