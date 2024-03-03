package edu.java.bot.scrapperclient;

import edu.java.bot.scrapperclient.models.AddLinkRequest;
import edu.java.bot.scrapperclient.models.LinkResponse;
import edu.java.bot.scrapperclient.models.ListLinksResponse;
import edu.java.bot.scrapperclient.models.RemoveLinkRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
public class ScrapperApiClient {
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String TG_CHAT_ID_PATH = "/tg-chat/{id}";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    public static final String LINKS_PATH = "/links";
    private final WebClient webClient;

    public ScrapperApiClient() {
        webClient = WebClient
            .builder()
            .baseUrl(DEFAULT_BASE_URL)
            .build();
    }

    public ScrapperApiClient(String baseUrl) {
        webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    public void registerChat(long chatId) {
        webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(TG_CHAT_ID_PATH).build(chatId))
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

    public void deleteChat(long chatId) {
        webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(TG_CHAT_ID_PATH).build(chatId))
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

    public ListLinksResponse getAllLinks(long chatId) {
        return webClient
            .get()
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
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
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(long chatId, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .body(Mono.just(addLinkRequest), AddLinkRequest.class)
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
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse deleteLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class)
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
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
