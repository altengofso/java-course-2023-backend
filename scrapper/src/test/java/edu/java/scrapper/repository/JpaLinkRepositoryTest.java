package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.chat.jpa.JpaChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private JpaSubscriptionRepository jpaSubscriptionRepository;

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addTest() {
        URI url = new URI("http://example.com");
        jpaLinkRepository.save(new LinkDto(url));
        assertThat(jpaLinkRepository.findByUrl(url)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void removeTest() {
        URI url = new URI("http://example.com");
        LinkDto added = jpaLinkRepository.save(new LinkDto(url));
        jpaLinkRepository.deleteById(added.getId());
        assertThat(jpaLinkRepository.findByUrl(url)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findAllTest() {
        LinkDto first = jpaLinkRepository.save(new LinkDto(new URI("http://example.com")));
        LinkDto second = jpaLinkRepository.save(new LinkDto(new URI("http://example2.com")));
        List<LinkDto> expected = List.of(first, second);
        List<LinkDto> actual = jpaLinkRepository.findAll();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void getAllLinksTest() {
        long chatId = 1L;
        jpaChatRepository.save(new ChatDto(chatId));
        LinkDto first = jpaLinkRepository.save(new LinkDto(new URI("http://example.com")));
        LinkDto second = jpaLinkRepository.save(new LinkDto(new URI("http://example2.com")));
        List<LinkDto> expected = List.of(first, second);
        expected.forEach(linkDto -> jpaSubscriptionRepository.save(new SubscriptionDto(linkDto.getId(), chatId)));
        List<LinkDto> actual = jpaLinkRepository.getAllLinks(chatId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findByUrlTest() {
        URI url = new URI("http://example.com");
        LinkDto expected = jpaLinkRepository.save(new LinkDto(url));
        LinkDto actual = jpaLinkRepository.findByUrl(url).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findAllByLastCheckAtIsLessThanOrLastCheckAtIsNullTest() {
        LinkDto expected = jpaLinkRepository.save(new LinkDto(new URI("http://example.com")));
        List<LinkDto> actual =
            jpaLinkRepository.findAllByLastCheckAtIsLessThanOrLastCheckAtIsNull(OffsetDateTime.now());
        assertThat(actual).containsExactly(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void setUpdatedAtAndLastCheckAtByIdTest() {
        URI url = new URI("http://example.com");
        LinkDto added = jpaLinkRepository.save(new LinkDto(url));
        OffsetDateTime updatedAt = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        OffsetDateTime lastCheckAt = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        added.setLastCheckAt(lastCheckAt);
        added.setUpdatedAt(updatedAt);
        jpaLinkRepository.save(added);
        LinkDto actual = jpaLinkRepository.findByUrl(url).orElseThrow();
        assertThat(actual.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(actual.getLastCheckAt()).isEqualTo(lastCheckAt);
    }
}
