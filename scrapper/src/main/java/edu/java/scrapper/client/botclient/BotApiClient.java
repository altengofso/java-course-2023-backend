package edu.java.scrapper.client.botclient;

import edu.java.scrapper.client.botclient.filter.ErrorLoggingFilter;
import edu.java.scrapper.client.botclient.filter.RetryFilter;
import edu.java.scrapper.client.botclient.models.LinkUpdate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotApiClient {
    private final WebClient webClient;

    public BotApiClient(String baseUrl, RetryFilter retryFilter) {
        var builder = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(new ErrorLoggingFilter());
        if (retryFilter != null) {
            builder.filter(retryFilter);
        }
        webClient = builder.build();
    }

    public void sendUpdates(LinkUpdate linkUpdate) {
        webClient
            .post()
            .uri("/updates")
            .body(Mono.just(linkUpdate), LinkUpdate.class)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
