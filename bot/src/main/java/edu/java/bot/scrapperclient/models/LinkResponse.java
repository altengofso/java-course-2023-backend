package edu.java.bot.scrapperclient.models;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url
) {
}
