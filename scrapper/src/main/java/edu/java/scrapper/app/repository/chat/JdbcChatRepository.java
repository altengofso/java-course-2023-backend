package edu.java.scrapper.app.repository.chat;

import edu.java.scrapper.app.repository.dto.ChatDto;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public void add(long chatId) {
        jdbcClient.sql("insert into chat(id) values (?, ?)")
            .param(List.of(chatId, OffsetDateTime.now()))
            .update();
    }

    @Override
    @Transactional
    public void remove(long chatId) {
        jdbcClient.sql("delete from chat where id = :id")
            .param("id", chatId)
            .query();
    }

    @Override
    @Transactional
    public List<ChatDto> findAll() {
        return jdbcClient.sql("select * from chat")
            .query(ChatDto.class)
            .list();
    }

    @Override
    public Optional<ChatDto> findById(long chatId) {
        return jdbcClient.sql("select * from chat where id = :chatId")
            .param("chatId", chatId)
            .query(ChatDto.class)
            .optional();
    }
}
