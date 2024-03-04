package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.exceptions.ConflictException;
import edu.java.scrapper.api.exceptions.ExceptionMessage;
import edu.java.scrapper.api.exceptions.NotFoundException;
import edu.java.scrapper.api.models.AddLinkRequest;
import edu.java.scrapper.api.models.LinkResponse;
import edu.java.scrapper.api.models.ListLinksResponse;
import edu.java.scrapper.api.models.RemoveLinkRequest;
import edu.java.scrapper.api.service.LinksService;
import edu.java.scrapper.api.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinksController implements LinksApi {
    private final LinksService linksService;
    private final TgChatService tgChatService;

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinks(long id) {
        if (!tgChatService.findById(id)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        return ResponseEntity.ok(linksService.getAllLinks(id));
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(long id, AddLinkRequest addLinkRequest) {
        if (!tgChatService.findById(id)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        if (linksService.findById(id, addLinkRequest.link())) {
            throw new ConflictException(ExceptionMessage.LINK_CONFLICT_MESSAGE.formatted(addLinkRequest.link()));
        }
        return ResponseEntity.ok(linksService.addLink(id, addLinkRequest.link()));
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(long id, RemoveLinkRequest removeLinkRequest) {
        if (!tgChatService.findById(id)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        if (!linksService.findById(id, removeLinkRequest.link())) {
            throw new NotFoundException(ExceptionMessage.LINK_NOTFOUND_MESSAGE);
        }
        return ResponseEntity.ok(linksService.deleteLink(id, removeLinkRequest.link()));
    }
}
