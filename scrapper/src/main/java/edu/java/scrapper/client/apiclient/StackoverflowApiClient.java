package edu.java.scrapper.client.apiclient;

import edu.java.scrapper.client.dto.ClientResponse;
import edu.java.scrapper.client.dto.StackoverflowDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackoverflowApiClient extends AbstractApiClient {
    private static final String DEFAULT_URL = "https://api.stackexchange.com";
    private static final Pattern EXTRACT_PATTERN = Pattern.compile("https?://.*/questions/(\\d+).*");
    private static final String URL_PREFIX = "https://stackoverflow.com";

    public StackoverflowApiClient() {
        super(DEFAULT_URL, URL_PREFIX);
    }

    public StackoverflowApiClient(String baseUrl) {
        super(baseUrl, DEFAULT_URL, URL_PREFIX);
    }

    @Override
    public ClientResponse getResponse(String link) {
        Matcher matcher = EXTRACT_PATTERN.matcher(link);
        if (!matcher.find()) {
            return null;
        }
        String id = matcher.group(1);
        StackoverflowDTO response = webClient
            .get()
            .uri("/questions/" + id + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackoverflowDTO.class)
            .block();
        return response == null ? null : new ClientResponse(response.items().getFirst().updatedAt());
    }
}
