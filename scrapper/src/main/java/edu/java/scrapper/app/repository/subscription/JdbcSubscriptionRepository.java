package edu.java.scrapper.app.repository.subscription;

import edu.java.scrapper.app.repository.dto.SubscriptionDto;
import java.time.OffsetDateTime;
import java.util.List;
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
    public void add(long linkId, long chatId) {
        jdbcClient.sql("insert into subscription (link_id, chat_id, created_at) values (?, ?, ?)")
            .param(List.of(linkId, chatId, OffsetDateTime.now()))
            .update();
    }

    @Override
    @Transactional
    public void remove(long linkId, long chatId) {
        jdbcClient.sql("delete from subscription where link_id = ? and chat_id = ?")
            .param(List.of(linkId, chatId))
            .update();
    }

    @Override
    @Transactional
    public List<SubscriptionDto> findAll() {
        return jdbcClient.sql("select * from subscription")
            .query(SubscriptionDto.class)
            .list();
    }

    @Override
    @Transactional
    public List<SubscriptionDto> findByChatId(long chatId) {
        return jdbcClient.sql("select * from subscription where chat_id = :chatId")
            .param("chatId", chatId)
            .query(SubscriptionDto.class)
            .list();
    }
}
