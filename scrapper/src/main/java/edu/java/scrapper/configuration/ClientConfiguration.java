package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackoverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubClient getGithubClient(ApplicationConfig applicationConfig) {
        return new GithubClient(applicationConfig.clientBaseUrl().githubUrl());
    }

    @Bean
    public StackoverflowClient getStackoverflowClient(ApplicationConfig applicationConfig) {
        return new StackoverflowClient(applicationConfig.clientBaseUrl().stackoverflowUrl());
    }
}
