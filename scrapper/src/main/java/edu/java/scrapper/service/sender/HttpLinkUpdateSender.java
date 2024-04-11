package edu.java.scrapper.service.sender;

import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.client.botclient.models.LinkUpdate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpLinkUpdateSender implements LinkUpdateSenderService {
    private final BotApiClient botApiClient;

    @Override
    public void send(LinkUpdate update) {
        botApiClient.sendUpdates(update);
    }
}
