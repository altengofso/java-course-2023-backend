package edu.java.scrapper.api.service;

import edu.java.scrapper.api.models.LinkResponse;
import edu.java.scrapper.api.models.ListLinksResponse;
import java.net.URI;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class LinksService {
    public ListLinksResponse getAllLinks(long chatId) {
        throw new NotImplementedException("Получение списка не реализовано");
    }

    public LinkResponse addLink(long chatId, URI link) {
        throw new NotImplementedException("Добавление не реализовано");
    }

    public LinkResponse deleteLink(long chatId, URI link) {
        throw new NotImplementedException("Удаление не реализовано");
    }

    public boolean findById(long chatId, URI link) {
        return true;
    }
}
