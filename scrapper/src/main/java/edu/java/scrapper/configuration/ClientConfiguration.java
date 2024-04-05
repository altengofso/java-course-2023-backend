package edu.java.scrapper.configuration;

import edu.java.scrapper.client.apiclient.GithubApiClient;
import edu.java.scrapper.client.apiclient.StackoverflowApiClient;
import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.client.botclient.filter.ConstantRetryFilter;
import edu.java.scrapper.client.botclient.filter.ExponentialRetryFilter;
import edu.java.scrapper.client.botclient.filter.LinearRetryFilter;
import edu.java.scrapper.client.botclient.filter.RetryFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GithubApiClient getGithubClient(ApplicationConfig applicationConfig) {
        return new GithubApiClient(applicationConfig.clientBaseUrl().githubUrl(), getRetryFilter(applicationConfig));
    }

    @Bean
    public StackoverflowApiClient getStackoverflowClient(ApplicationConfig applicationConfig) {
        return new StackoverflowApiClient(
            applicationConfig.clientBaseUrl().stackoverflowUrl(),
            getRetryFilter(applicationConfig)
        );
    }

    @Bean
    public BotApiClient getBotApiClient(ApplicationConfig applicationConfig) {
        return new BotApiClient(applicationConfig.clientBaseUrl().botUrl(), getRetryFilter(applicationConfig));
    }

    private RetryFilter getRetryFilter(ApplicationConfig applicationConfig) {
        return switch (applicationConfig.retryPolicy().type()) {
            case NONE -> null;
            case CONSTANT -> new ConstantRetryFilter(applicationConfig);
            case LINEAR -> new LinearRetryFilter(applicationConfig);
            case EXPONENTIAL -> new ExponentialRetryFilter(applicationConfig);
        };
    }
}
