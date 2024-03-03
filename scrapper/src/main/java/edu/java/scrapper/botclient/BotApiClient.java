package edu.java.scrapper.botclient;

import edu.java.scrapper.botclient.models.LinkUpdate;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
public class BotApiClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8090";
    private final WebClient webClient;

    public BotApiClient() {
        webClient = WebClient
            .builder()
            .baseUrl(DEFAULT_BASE_URL)
            .build();
    }

    public BotApiClient(String baseUrl) {
        webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    public void sendUpdates(LinkUpdate linkUpdate) {
        webClient
            .post()
            .uri("/updates")
            .body(Mono.just(linkUpdate), LinkUpdate.class)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> clientResponse.bodyToMono(String.class)
                    .flatMap(error -> {
                        log.error(error);
                        throw new HttpClientErrorException(
                            clientResponse.statusCode(), error
                        );
                    })
            )
            .bodyToMono(String.class)
            .block();
    }
}
