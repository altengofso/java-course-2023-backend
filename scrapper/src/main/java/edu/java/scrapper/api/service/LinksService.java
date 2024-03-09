package edu.java.scrapper.api.service;

import edu.java.scrapper.api.exceptions.ConflictException;
import edu.java.scrapper.api.exceptions.ExceptionMessage;
import edu.java.scrapper.api.exceptions.NotFoundException;
import edu.java.scrapper.api.models.LinkResponse;
import edu.java.scrapper.api.models.ListLinksResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinksService {
    private final TgChatService tgChatService;

    public ListLinksResponse getAllLinks(long chatId) {
        if (!tgChatService.verifyChatExistence(chatId)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        return null;
    }

    public LinkResponse addLink(long chatId, URI link) {
        if (!tgChatService.verifyChatExistence(chatId)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        if (verifyLinkExistance(chatId, link)) {
            throw new ConflictException(ExceptionMessage.LINK_CONFLICT_MESSAGE.formatted(link));
        }
        return null;
    }

    public LinkResponse deleteLink(long chatId, URI link) {
        if (!tgChatService.verifyChatExistence(chatId)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        if (!verifyLinkExistance(chatId, link)) {
            throw new NotFoundException(ExceptionMessage.LINK_NOTFOUND_MESSAGE);
        }
        return null;
    }

    public boolean verifyLinkExistance(long chatId, URI link) {
        return true;
    }
}
