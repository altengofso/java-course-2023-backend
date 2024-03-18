package edu.java.scrapper.controller.dto;

import java.util.List;

public record ListLinkResponse(List<LinkResponse> links, int size) {
}
