package edu.java.bot.configuration;

import edu.java.bot.processor.GitHubURIProcessor;
import edu.java.bot.processor.StackOverflowURIProcessor;
import edu.java.bot.processor.URIProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class URIProcessorConfig {
    @Bean
    URIProcessor uriProcessor() {
        return new StackOverflowURIProcessor(
            new GitHubURIProcessor(null)
        );
    }
}
