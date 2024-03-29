package edu.java.bot.app;

import edu.java.bot.app.models.uri.ValidUri;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UriValidator {
    private final List<ValidUri> validUris;

    public boolean isValidUri(URI uri) {
        return validUris
            .stream()
            .anyMatch(validUri -> uri.getHost() != null && uri.getHost().equals(validUri.getHost()));
    }
}
