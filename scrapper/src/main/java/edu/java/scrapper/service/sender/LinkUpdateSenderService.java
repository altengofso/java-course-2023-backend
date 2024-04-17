package edu.java.scrapper.service.sender;

import edu.java.scrapper.client.botclient.models.LinkUpdate;

public interface LinkUpdateSenderService {
    void send(LinkUpdate update);
}
