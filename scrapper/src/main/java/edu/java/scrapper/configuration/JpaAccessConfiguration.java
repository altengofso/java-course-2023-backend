package edu.java.scrapper.configuration;

import edu.java.scrapper.client.apiclient.ApiClient;
import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.repository.chat.jpa.JpaChatRepository;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdaterService;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import edu.java.scrapper.service.jpa.JpaLinkUpdaterService;
import edu.java.scrapper.service.jpa.JpaTgChatService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public TgChatService tgChatService(JpaChatRepository chatRepository) {
        return new JpaTgChatService(chatRepository);
    }

    @Bean
    public LinkService linkService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        JpaSubscriptionRepository subscriptionRepository
    ) {
        return new JpaLinkService(chatRepository, linkRepository, subscriptionRepository);
    }

    @Bean
    public LinkUpdaterService linkUpdaterService(
        JpaLinkRepository linkRepository,
        JpaSubscriptionRepository subscriptionRepository,
        List<ApiClient> apiClients,
        BotApiClient botApiClient
    ) {
        return new JpaLinkUpdaterService(linkRepository, subscriptionRepository, apiClients, botApiClient);
    }
}
