package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.chat.jpa.JpaChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        jpaChatRepository.save(new ChatDto(1L));
        assertThat(jpaChatRepository.findById(1L)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        jpaChatRepository.save(new ChatDto(1L));
        jpaChatRepository.deleteById(1L);
        assertThat(jpaChatRepository.findById(1L)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        ChatDto first = jpaChatRepository.save(new ChatDto(1L));
        ChatDto second = jpaChatRepository.save(new ChatDto(2L));
        List<ChatDto> expected = List.of(first, second);
        List<ChatDto> actual = jpaChatRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    void findByIdTest() {
        ChatDto expected = jpaChatRepository.save(new ChatDto(1L));
        ChatDto actual = jpaChatRepository.findById(1L).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }
}
