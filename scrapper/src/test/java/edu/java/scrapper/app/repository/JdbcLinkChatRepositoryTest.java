package edu.java.scrapper.app.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.chat.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcLinkChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jdbcChatRepository.add(1L);
        assertThat(jdbcChatRepository.findById(1L)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jdbcChatRepository.add(1L);
        ChatDto expected = jdbcChatRepository.findById(1L).orElseThrow();
        ChatDto actual = jdbcChatRepository.remove(1L);
        assertThat(actual).isEqualTo(expected);
        assertThat(jdbcChatRepository.findById(1L)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        ChatDto first = jdbcChatRepository.add(1L);
        ChatDto second = jdbcChatRepository.add(2L);
        List<ChatDto> expected = List.of(first, second);
        List<ChatDto> actual = jdbcChatRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        ChatDto expected = jdbcChatRepository.add(1L);
        ChatDto actual = jdbcChatRepository.findById(1L).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }
}
