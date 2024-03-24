package edu.java.bot.scrapperclient;

import edu.java.bot.scrapperclient.filter.ErrorLoggingFilter;
import edu.java.bot.scrapperclient.models.AddLinkRequest;
import edu.java.bot.scrapperclient.models.ChatResponse;
import edu.java.bot.scrapperclient.models.LinkResponse;
import edu.java.bot.scrapperclient.models.ListLinksResponse;
import edu.java.bot.scrapperclient.models.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperApiClient {
    private static final String TG_CHAT_ID_PATH = "/tg-chat/{id}";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String LINKS_PATH = "/links";
    private final WebClient webClient;

    public ScrapperApiClient(String baseUrl) {
        webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .filter(new ErrorLoggingFilter())
            .build();
    }

    public ChatResponse getChat(long chatId) {
        return webClient
            .get()
            .uri(uriBuilder -> uriBuilder.path(TG_CHAT_ID_PATH).build(chatId))
            .retrieve()
            .bodyToMono(ChatResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block();
    }

    public ChatResponse registerChat(long chatId) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(TG_CHAT_ID_PATH).build(chatId))
            .retrieve()
            .bodyToMono(ChatResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block();
    }

    public ChatResponse deleteChat(long chatId) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(TG_CHAT_ID_PATH).build(chatId))
            .retrieve()
            .bodyToMono(ChatResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block();
    }

    public ListLinksResponse getAllLinks(long chatId) {
        return webClient
            .get()
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block();
    }

    public LinkResponse addLink(long chatId, AddLinkRequest addLinkRequest) {
        return webClient
            .post()
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .body(Mono.just(addLinkRequest), AddLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block();
    }

    public LinkResponse deleteLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(LINKS_PATH)
            .header(TG_CHAT_ID_HEADER, String.valueOf(chatId))
            .body(Mono.just(removeLinkRequest), RemoveLinkRequest.class)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block();
    }
}
