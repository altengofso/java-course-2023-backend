package edu.java.bot.scrapperclient.models;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
}
