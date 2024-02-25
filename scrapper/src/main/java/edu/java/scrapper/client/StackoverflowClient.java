package edu.java.scrapper.client;

import edu.java.scrapper.client.dto.StackoverflowDTO;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Log4j2
public class StackoverflowClient implements ResourceClient {
    private static final String DEFAULT_URL = "https://api.stackexchange.com";
    private static final Pattern PATTERN = Pattern.compile("https?://.*/questions/(\\d+).*");
    private final WebClient webClient;

    public StackoverflowClient(String baseUrl) {
        webClient = WebClient
            .builder()
            .baseUrl(Objects.requireNonNullElse(baseUrl, DEFAULT_URL))
            .build();
    }

    @Override
    public OffsetDateTime getUpdatedAt(String link) {
        Matcher matcher = PATTERN.matcher(link);
        if (!matcher.find()) {
            return null;
        }
        String id = matcher.group(1);
        StackoverflowDTO response = null;
        try {
            response = webClient
                .get()
                .uri("/questions/" + id + "?site=stackoverflow")
                .retrieve()
                .bodyToMono(StackoverflowDTO.class)
                .block();
        } catch (WebClientRequestException e) {
            log.error(e.getMessage());
        }
        return response == null ? null : response.items().getFirst().updatedAt();
    }
}
