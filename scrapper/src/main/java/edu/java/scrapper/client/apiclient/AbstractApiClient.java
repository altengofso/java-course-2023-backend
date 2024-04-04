package edu.java.scrapper.client.apiclient;

import edu.java.scrapper.client.botclient.filter.RetryFilter;
import java.util.regex.Pattern;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractApiClient implements ApiClient {
    protected String urlPrefix;
    protected Pattern extractPattern;
    protected final WebClient webClient;

    public AbstractApiClient(String baseUrl, String urlPrefix, Pattern extractPattern, RetryFilter retryFilter) {
        this.urlPrefix = urlPrefix;
        this.extractPattern = extractPattern;
        var builder = WebClient
            .builder()
            .baseUrl(baseUrl);
        if (retryFilter != null) {
            builder.filter(retryFilter);
        }
        webClient = builder.build();
    }

    @Override
    public boolean canAccess(String link) {
        return link.startsWith(urlPrefix) && extractPattern.matcher(link).find();
    }
}
