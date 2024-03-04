package edu.java.scrapper.api.service;

import edu.java.scrapper.api.models.LinkResponse;
import edu.java.scrapper.api.models.ListLinksResponse;
import java.net.URI;
import org.springframework.stereotype.Service;

@Service
public class LinksService {
    public ListLinksResponse getAllLinks(long chatId) {
        return null;
    }

    public LinkResponse addLink(long chatId, URI link) {
        return null;
    }

    public LinkResponse deleteLink(long chatId, URI link) {
        return null;
    }

    public boolean findById(long chatId, URI link) {
        return true;
    }
}
