package edu.java.scrapper.app.client;

import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractApiClient implements ApiClient {
    private final String defaultUrl;
    protected String urlPrefix;
    protected final WebClient webClient;

    public AbstractApiClient(String baseUrl, String defaultUrl, String urlPrefix) {
        this.defaultUrl = defaultUrl;
        this.urlPrefix = urlPrefix;
        webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(baseUrl, defaultUrl))
            .build();
    }

    @Override
    public boolean canAccess(String link) {
        return link.startsWith(urlPrefix);
    }
}
