package edu.java.scrapper.app.client;

import edu.java.scrapper.app.client.dto.ClientResponse;
import edu.java.scrapper.app.client.dto.GithubDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubApiClient extends AbstractApiClient {
    private static final String DEFAULT_URL = "https://api.github.com";
    private static final Pattern EXTRACT_PATTERN = Pattern.compile("https?://.*/(.+)/(.+).*");
    private static final String URL_PREFIX = "https://github.com";

    public GithubApiClient() {
        super(DEFAULT_URL, URL_PREFIX);
    }

    public GithubApiClient(String baseUrl) {
        super(baseUrl, DEFAULT_URL, URL_PREFIX);
    }

    @Override
    public ClientResponse getResponse(String link) {
        Matcher matcher = EXTRACT_PATTERN.matcher(link);
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
