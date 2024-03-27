package edu.java.scrapper.repository.subscription.jdbc;

import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.subscription.SubscriptionRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public SubscriptionDto add(long linkId, long chatId) {
        return jdbcClient.sql("""
                insert into subscription (link_id, chat_id, created_at) values (?, ?, ?)
                on conflict do nothing returning *""")
            .param(linkId)
            .param(chatId)
            .param(OffsetDateTime.now())
            .query(SubscriptionDto.class)
            .single();
    }

    @Override
    @Transactional
    public void remove(long linkId, long chatId) {
        jdbcClient.sql("delete from subscription where link_id = ? and chat_id = ?")
            .param(linkId)
            .param(chatId)
            .update();
    }

    @Override
    @Transactional
    public Optional<SubscriptionDto> findByLinkIdAndChatId(long linkId, long chatId) {
        return jdbcClient.sql("select * from subscription where link_id = ? and chat_id = ?")
            .param(linkId)
            .param(chatId)
            .query(SubscriptionDto.class)
            .optional();
    }

    @Override
    @Transactional
    public List<SubscriptionDto> findAllByLinkId(long linkId) {
        return jdbcClient.sql("select * from subscription where link_id = ?")
            .param(linkId)
            .query(SubscriptionDto.class)
            .list();
    }
}
