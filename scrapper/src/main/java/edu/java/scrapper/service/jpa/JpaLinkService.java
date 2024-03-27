package edu.java.scrapper.service.jpa;

import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.ListLinkResponse;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.ExceptionMessage;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.chat.jpa.JpaChatRepository;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.dto.SubscriptionId;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaSubscriptionRepository subscriptionRepository;

    @Override
    public ListLinkResponse getAllLinks(long chatId) {
        if (chatRepository.findById(chatId).isEmpty()) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        var linkList = linkRepository.getAllLinks(chatId)
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
        var link = optionalLinkDto.orElseGet(() -> linkRepository.save(new LinkDto(url)));
        subscriptionRepository.save(new SubscriptionDto(link.getId(), chatId));
        return new LinkResponse(link.getId(), link.getUrl());
    }

    @Override
    public LinkResponse deleteLink(long chatId, URI url) {
        if (!verifyLinkExistance(chatId, url)) {
            throw new NotFoundException(ExceptionMessage.LINK_NOTFOUND_MESSAGE);
        }
        var link = linkRepository.findByUrl(url).orElseThrow();
        subscriptionRepository.deleteById(new SubscriptionId(link.getId(), chatId));
        if (subscriptionRepository.findAllByLinkId(link.getId()).isEmpty()) {
            linkRepository.deleteById(link.getId());
        }
        return new LinkResponse(link.getId(), url);
    }

    @Override
    public boolean verifyLinkExistance(long chatId, URI url) {
        var link = linkRepository.findByUrl(url);
        return link.filter(linkDto -> subscriptionRepository.findById(new SubscriptionId(linkDto.getId(), chatId))
            .isPresent()).isPresent();
    }
}
