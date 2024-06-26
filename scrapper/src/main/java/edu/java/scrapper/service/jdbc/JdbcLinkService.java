package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.ListLinkResponse;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.ExceptionMessage;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.chat.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.subscription.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcSubscriptionRepository subscriptionRepository;

    @Override
    public ListLinkResponse getAllLinks(long chatId) {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        var linkList = linkRepository.findAllByChatId(chatId)
            .stream()
            .map(linkDto -> new LinkResponse(linkDto.getId(), linkDto.getUrl()))
            .toList();
        return new ListLinkResponse(linkList, linkList.size());
    }

    @Override
    public LinkResponse addLink(long chatId, URI url) {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        if (verifyLinkExistance(chatId, url)) {
            throw new ConflictException(ExceptionMessage.LINK_CONFLICT_MESSAGE.formatted(url));
        }
        Optional<LinkDto> optionalLinkDto = linkRepository.findByUrl(url);
        var link = optionalLinkDto.orElseGet(() -> linkRepository.add(url));
        subscriptionRepository.add(link.getId(), chatId);
        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public LinkResponse deleteLink(long chatId, URI url) {
        if (!verifyLinkExistance(chatId, url)) {
            throw new NotFoundException(ExceptionMessage.LINK_NOTFOUND_MESSAGE);
        }
        var link = linkRepository.findByUrl(url).orElseThrow();
        subscriptionRepository.remove(link.getId(), chatId);
        if (subscriptionRepository.findAllByLinkId(link.getId()).isEmpty()) {
            linkRepository.remove(link.getId());
        }
        return new LinkResponse(link.getId(), url);
    }

    @Override
    public boolean verifyLinkExistance(long chatId, URI url) {
        var link = linkRepository.findByUrl(url);
        return link.filter(linkDto -> subscriptionRepository.findByLinkIdAndChatId(linkDto.getId(), chatId).isPresent())
            .isPresent();
    }
}
