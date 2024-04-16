package edu.java.scrapper.configuration;

import edu.java.scrapper.client.apiclient.ApiClient;
import edu.java.scrapper.repository.chat.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.subscription.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdaterService;
import edu.java.scrapper.service.TgChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcLinkUpdaterService;
import edu.java.scrapper.service.jdbc.JdbcTgChatService;
import edu.java.scrapper.service.sender.LinkUpdateSenderService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public TgChatService tgChatService(JdbcChatRepository chatRepository) {
        return new JdbcTgChatService(chatRepository);
    }

    @Bean
    public LinkService linkService(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcSubscriptionRepository subscriptionRepository
    ) {
        return new JdbcLinkService(chatRepository, linkRepository, subscriptionRepository);
    }

    @Bean
    public LinkUpdaterService linkUpdaterService(
        JdbcLinkRepository linkRepository,
        JdbcSubscriptionRepository subscriptionRepository,
        List<ApiClient> apiClients,
        LinkUpdateSenderService linkUpdateSenderService
    ) {
        return new JdbcLinkUpdaterService(linkRepository, subscriptionRepository, apiClients, linkUpdateSenderService);
    }
}
