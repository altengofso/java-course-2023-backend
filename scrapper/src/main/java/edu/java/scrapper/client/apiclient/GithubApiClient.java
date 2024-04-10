package edu.java.scrapper.client.apiclient;

import edu.java.scrapper.client.botclient.filter.RetryFilter;
import edu.java.scrapper.client.dto.ClientResponse;
import edu.java.scrapper.client.dto.GithubDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubApiClient extends AbstractApiClient {
    private static final Pattern EXTRACT_PATTERN = Pattern.compile("https?://.*/(.+)/(.+).*");
    private static final String URL_PREFIX = "https://github.com";

    public GithubApiClient(String baseUrl, RetryFilter retryFilter) {
        super(baseUrl, URL_PREFIX, EXTRACT_PATTERN, retryFilter);
    }

    @Override
    public ClientResponse getResponse(String link) {
        Matcher matcher = extractPattern.matcher(link);
        if (!matcher.find()) {
            return null;
        }
        String login = matcher.group(1);
        String repo = matcher.group(2);
        GithubDTO response = webClient
            .get()
            .uri("/repos/" + login + "/" + repo)
            .retrieve()
            .bodyToMono(GithubDTO.class)
            .block();
        return response == null ? null : new ClientResponse(response.updatedAt());
    }
}
