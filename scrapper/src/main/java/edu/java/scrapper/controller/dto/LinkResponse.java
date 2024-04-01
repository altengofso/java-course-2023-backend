package edu.java.scrapper.controller.dto;

import java.net.URI;

public record LinkResponse(
    long id,
    URI url
) {
}
