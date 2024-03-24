package edu.java.scrapper.service.jpa;

import edu.java.scrapper.client.apiclient.ApiClient;
import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.client.botclient.models.LinkUpdate;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import edu.java.scrapper.service.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkUpdaterService implements LinkUpdaterService {
    private static final String DESCRIPTION_FORMAT = "Ссылка %s была обновлена %s";
    private final JpaLinkRepository linkRepository;
    private final JpaSubscriptionRepository subscriptionRepository;
    private final List<ApiClient> apiClients;
    private final BotApiClient botApiClient;

    @Override
    public void update(OffsetDateTime lastCheckAt) {
        var links = linkRepository.findAllByLastCheckAtIsLessThanOrLastCheckAtIsNull(lastCheckAt);
        Map<LinkDto, List<Long>> updates = checkUpdates(links);
        sendUpdates(updates);
    }

    private Map<LinkDto, List<Long>> checkUpdates(List<LinkDto> links) {
        Map<LinkDto, List<Long>> updates = new HashMap<>();
        for (var link : links) {
            var client = apiClients
                .stream()
                .filter(apiClient -> apiClient.canAccess(link.getUrl().toString()))
                .findFirst();
            if (client.isPresent()) {
                var clientResponse = client.get().getResponse(link.getUrl().toString());
                OffsetDateTime newLastCheckedAt = OffsetDateTime.now(ZoneOffset.UTC);
                OffsetDateTime newUpdatedAt = clientResponse.updateAt();
                if (link.getUpdatedAt() == null || link.getUpdatedAt().isBefore(newUpdatedAt)) {
                    link.setUpdatedAt(newUpdatedAt);
                    link.setLastCheckAt(newLastCheckedAt);
                    LinkDto updatedLink = linkRepository.save(link);
                    updates.put(
                        updatedLink,
                        subscriptionRepository
                            .findAllByLinkId(updatedLink.getId())
                            .stream()
                            .map(SubscriptionDto::getChatId)
                            .toList()
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
                link.getUrl().toString(),
                link.getUpdatedAt().toLocalDateTime().toString()
            );
            botApiClient.sendUpdates(new LinkUpdate(link.getId(), link.getUrl(), description, tgChatIds));
        }
    }
}
