package edu.java.bot.processor;

import java.net.URI;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class URIProcessor {
    protected final URIProcessor nextURIProcessor;

    protected abstract String getValidHostName();

    public final boolean isValidURI(URI uri) {
        if (uri.getHost() != null && uri.getHost().equals(getValidHostName())) {
            return true;
        }
        if (nextURIProcessor != null) {
            return nextURIProcessor.isValidURI(uri);
        }
        return false;
    }
}
