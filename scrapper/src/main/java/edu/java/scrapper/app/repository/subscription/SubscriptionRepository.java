package edu.java.scrapper.app.repository.subscription;

import edu.java.scrapper.app.repository.dto.SubscriptionDto;
import java.util.List;

public interface SubscriptionRepository {
    void add(long linkId, long chatId);

    void remove(long linkId, long chatId);

    List<SubscriptionDto> findAll();

    List<SubscriptionDto> findByChatId(long chatId);
}
