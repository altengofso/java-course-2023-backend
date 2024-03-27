package edu.java.scrapper.app.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addTest() {
        URI url = new URI("http://example.com");
        jdbcLinkRepository.add(url);
        assertThat(jdbcLinkRepository.findByUrl(url)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void removeTest() {
        URI url = new URI("http://example.com");
        LinkDto added = jdbcLinkRepository.add(url);
        jdbcLinkRepository.remove(added.getId());
        assertThat(jdbcLinkRepository.findByUrl(url)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findAllTest() {
        LinkDto first = jdbcLinkRepository.add(new URI("http://example.com"));
        LinkDto second = jdbcLinkRepository.add(new URI("http://example2.com"));
        List<LinkDto> expected = List.of(first, second);
        List<LinkDto> actual = jdbcLinkRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findAllByChatIdTest() {
        long chatId = 1L;
        jdbcClient.sql("insert into chat(id) values(?)").param(chatId).update();
        LinkDto first = jdbcLinkRepository.add(new URI("http://example.com"));
        LinkDto second = jdbcLinkRepository.add(new URI("http://example2.com"));
        List<LinkDto> expected = List.of(first, second);
        expected.forEach(linkDto -> jdbcClient.sql("insert into subscription(link_id, chat_id) values (?, ?)")
            .param(linkDto.getId())
            .param(1L)
            .update());
        List<LinkDto> actual = jdbcLinkRepository.findAllByChatId(chatId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findByUrlTest() {
        URI url = new URI("http://example.com");
        LinkDto expected = jdbcLinkRepository.add(url);
        LinkDto actual = jdbcLinkRepository.findByUrl(url).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findByLastCheckAtLessThanOrNullTest() {
        LinkDto expected = jdbcLinkRepository.add(new URI("http://example.com"));
        List<LinkDto> actual = jdbcLinkRepository.findByLastCheckAtLessThanOrNull(OffsetDateTime.now());
        assertThat(actual).containsExactly(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void setUpdatedAtAndLastCheckAtByIdTest() {
        URI url = new URI("http://example.com");
        LinkDto added = jdbcLinkRepository.add(url);
        OffsetDateTime updatedAt = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        OffsetDateTime lastCheckAt = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        jdbcLinkRepository.setUpdatedAtAndLastCheckAtById(added.getId(), updatedAt, lastCheckAt);
        LinkDto actual = jdbcLinkRepository.findByUrl(url).orElseThrow();
        assertThat(actual.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(actual.getLastCheckAt()).isEqualTo(lastCheckAt);
    }
}
