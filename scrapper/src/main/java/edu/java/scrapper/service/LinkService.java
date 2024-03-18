package edu.java.scrapper.service;

import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.ListLinkResponse;
import java.net.URI;

public interface LinkService {
    ListLinkResponse getAllLinks(long chatId);

    LinkResponse addLink(long chatId, URI url);

    LinkResponse deleteLink(long chatId, URI url);

    boolean verifyLinkExistance(long chatId, URI url);
}
