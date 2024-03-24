package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.client.apiclient.ApiClient;
import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.client.botclient.models.LinkUpdate;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.subscription.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.service.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdaterService implements LinkUpdaterService {
    private static final String DESCRIPTION_FORMAT = "Ссылка %s была обновлена %s";
    private final JdbcLinkRepository linkRepository;
    private final JdbcSubscriptionRepository subscriptionRepository;
    private final List<ApiClient> apiClients;
    private final BotApiClient botApiClient;

    @Override
    public void update(OffsetDateTime lastCheckAt) {
        var links = linkRepository.findByLastCheckAtLessThanOrNull(lastCheckAt);
        Map<LinkDto, List<Long>> updates = checkUpdates(links);
        sendUpdates(updates);
    }

    private Map<LinkDto, List<Long>> checkUpdates(List<LinkDto> links) {
        Map<LinkDto, List<Long>> updates = new HashMap<>();
        for (var link : links) {
            var client = apiClients
                .stream()
                .filter(apiClient -> apiClient.canAccess(link.url().toString()))
                .findFirst();
            if (client.isPresent()) {
                var clientResponse = client.get().getResponse(link.url().toString());
                OffsetDateTime newLastCheckedAt = OffsetDateTime.now(ZoneOffset.UTC);
                OffsetDateTime newUpdatedAt = clientResponse.updateAt();
                if (link.updatedAt() == null || link.updatedAt().isBefore(newUpdatedAt)) {
                    LinkDto updatedLink = linkRepository.setUpdatedAtAndLastCheckAtById(
                        link.id(),
                        newUpdatedAt,
                        newLastCheckedAt
                    );
                    updates.put(
                        updatedLink,
                        subscriptionRepository.findAllByLinkId(link.id()).stream().map(SubscriptionDto::chatId).toList()
                    );
                }
            }

        }
        return updates;
    }

    private void sendUpdates(Map<LinkDto, List<Long>> updates) {
        for (var entry : updates.entrySet()) {
            LinkDto link = entry.getKey();
            List<Long> tgChatIds = entry.getValue();
            String description = DESCRIPTION_FORMAT.formatted(
                link.url().toString(),
                link.updatedAt().toLocalDateTime().toString()
            );
            botApiClient.sendUpdates(new LinkUpdate(link.id(), link.url(), description, tgChatIds));
        }
    }
}
