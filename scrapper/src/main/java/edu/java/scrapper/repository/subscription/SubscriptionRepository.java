package edu.java.scrapper.repository.subscription;

import edu.java.scrapper.repository.dto.SubscriptionDto;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    SubscriptionDto add(long linkId, long chatId);

    void remove(long linkId, long chatId);

    Optional<SubscriptionDto> findByLinkIdAndChatId(long linkId, long chatId);

    List<SubscriptionDto> findAllByLinkId(long linkId);
}
