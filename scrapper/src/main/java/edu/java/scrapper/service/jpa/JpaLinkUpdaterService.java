package edu.java.scrapper.service.jpa;

import edu.java.scrapper.client.apiclient.ApiClient;
import edu.java.scrapper.client.botclient.models.LinkUpdate;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import edu.java.scrapper.service.LinkUpdaterService;
import edu.java.scrapper.service.sender.LinkUpdateSenderService;
import java.time.OffsetDateTime;
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
    private final LinkUpdateSenderService linkUpdateSenderService;

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
                OffsetDateTime prevUpdatedAt = link.getUpdatedAt();
                link.setUpdatedAt(clientResponse.updateAt());
                link.setLastCheckAt(OffsetDateTime.now());
                LinkDto updatedLink = linkRepository.save(link);
                if (prevUpdatedAt == null || prevUpdatedAt.isBefore(updatedLink.getUpdatedAt())) {
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
            linkUpdateSenderService.send(new LinkUpdate(link.getId(), link.getUrl(), description, tgChatIds));
        }
    }
}
