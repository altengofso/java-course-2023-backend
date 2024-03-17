package edu.java.scrapper.app.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.app.repository.chat.JdbcChatRepository;
import edu.java.scrapper.app.repository.dto.ChatDto;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcLinkChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        long chatId = 1L;
        jdbcChatRepository.add(1L);
        ChatDto chatDto = jdbcChatRepository.findById(chatId).orElseThrow();
        assertThat(chatDto.id()).isEqualTo(chatId);
        OffsetDateTime now = OffsetDateTime.now();
        assertThat(chatDto.createdAt().toLocalDateTime()).isEqualTo(now.toLocalDateTime());
    }
}
