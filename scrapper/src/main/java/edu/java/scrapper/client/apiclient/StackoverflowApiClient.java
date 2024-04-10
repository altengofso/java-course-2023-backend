package edu.java.scrapper.client.apiclient;

import edu.java.scrapper.client.botclient.filter.RetryFilter;
import edu.java.scrapper.client.dto.ClientResponse;
import edu.java.scrapper.client.dto.StackoverflowDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackoverflowApiClient extends AbstractApiClient {
    private static final Pattern EXTRACT_PATTERN = Pattern.compile("https?://.*/questions/(\\d+).*");
    private static final String URL_PREFIX = "https://stackoverflow.com";

    public StackoverflowApiClient(String baseUrl, RetryFilter retryFilter) {
        super(baseUrl, URL_PREFIX, EXTRACT_PATTERN, retryFilter);
    }

    @Override
    public ClientResponse getResponse(String link) {
        Matcher matcher = extractPattern.matcher(link);
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
