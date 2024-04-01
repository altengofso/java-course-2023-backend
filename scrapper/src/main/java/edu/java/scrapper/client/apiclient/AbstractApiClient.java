package edu.java.scrapper.client.apiclient;

import java.util.regex.Pattern;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractApiClient implements ApiClient {
    private final String defaultUrl;
    protected String urlPrefix;
    protected Pattern extractPattern;
    protected final WebClient webClient;

    public AbstractApiClient(String defaultUrl, String urlPrefix, Pattern extractPattern) {
        this.defaultUrl = defaultUrl;
        this.urlPrefix = urlPrefix;
        this.extractPattern = extractPattern;
        webClient = WebClient
            .builder()
            .baseUrl(defaultUrl)
            .build();
    }

    public AbstractApiClient(String baseUrl, String defaultUrl, String urlPrefix, Pattern extractPattern) {
        this.defaultUrl = defaultUrl;
        this.urlPrefix = urlPrefix;
        this.extractPattern = extractPattern;
        webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    @Override
    public boolean canAccess(String link) {
        return link.startsWith(urlPrefix) && extractPattern.matcher(link).find();
    }
}
