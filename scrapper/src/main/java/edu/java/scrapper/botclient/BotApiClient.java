package edu.java.scrapper.botclient;

import edu.java.scrapper.botclient.filter.ErrorLoggingFilter;
import edu.java.scrapper.botclient.models.LinkUpdate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotApiClient {
    private final WebClient webClient;

    public BotApiClient(String baseUrl) {
        webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(new ErrorLoggingFilter())
            .build();
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
