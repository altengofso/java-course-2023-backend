package edu.java.scrapper.client.botclient.models;

import java.net.URI;
import java.util.List;

public record LinkUpdate(
    long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {
}
