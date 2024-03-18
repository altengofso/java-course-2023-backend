package edu.java.scrapper.repository.chat.jdbc;

import edu.java.scrapper.repository.chat.ChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    @Autowired
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public ChatDto add(long chatId) {
        return jdbcClient.sql("insert into chat(id, created_at) values (?, ?) on conflict do nothing returning *")
            .param(chatId)
            .param(OffsetDateTime.now(ZoneOffset.UTC))
            .query(ChatDto.class)
            .single();
    }

    @Override
    @Transactional
    public ChatDto remove(long chatId) {
        return jdbcClient.sql("delete from chat where id = ? returning *")
            .param(chatId)
            .query(ChatDto.class)
            .single();
    }

    @Override
    @Transactional
    public List<ChatDto> findAll() {
        return jdbcClient.sql("select * from chat")
            .query(ChatDto.class)
            .list();
    }

    @Override
    @Transactional
    public Optional<ChatDto> findById(long chatId) {
        return jdbcClient.sql("select * from chat where id = ?")
            .param(chatId)
            .query(ChatDto.class)
            .optional();
    }
}
