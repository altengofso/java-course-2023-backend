package edu.java.scrapper.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.chat.jpa.JpaChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.dto.SubscriptionId;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import java.net.URI;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JpaSubscriptionRepositoryTest extends IntegrationEnvironment {
    private static final String URL = "http://example.com";
    private static final long CHAT_ID = 1L;

    @Autowired
    private JpaSubscriptionRepository jpaSubscriptionRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addTest() {
        LinkDto linkDto = jpaLinkRepository.save(new LinkDto(new URI(URL)));
        jpaChatRepository.save(new ChatDto(CHAT_ID));
        jpaSubscriptionRepository.save(new SubscriptionDto(linkDto.getId(), CHAT_ID));
        assertThat(jpaSubscriptionRepository.findById(new SubscriptionId(linkDto.getId(), CHAT_ID))).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void removeTest() {
        LinkDto linkDto = jpaLinkRepository.save(new LinkDto(new URI(URL)));
        jpaChatRepository.save(new ChatDto(CHAT_ID));
        jpaSubscriptionRepository.save(new SubscriptionDto(linkDto.getId(), CHAT_ID));
        jpaSubscriptionRepository.deleteById(new SubscriptionId(linkDto.getId(), CHAT_ID));
        assertThat(jpaSubscriptionRepository.findById(new SubscriptionId(linkDto.getId(), CHAT_ID))).isNotPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findByIdTest() {
        LinkDto linkDto = jpaLinkRepository.save(new LinkDto(new URI(URL)));
        jpaChatRepository.save(new ChatDto(CHAT_ID));
        SubscriptionDto expected = jpaSubscriptionRepository.save(new SubscriptionDto(linkDto.getId(), CHAT_ID));
        SubscriptionDto actual =
            jpaSubscriptionRepository.findById(new SubscriptionId(linkDto.getId(), CHAT_ID)).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findAllByLinkIdTest() {
        LinkDto linkDto = jpaLinkRepository.save(new LinkDto(new URI(URL)));
        jpaChatRepository.save(new ChatDto(CHAT_ID));
        SubscriptionDto expected = jpaSubscriptionRepository.save(new SubscriptionDto(linkDto.getId(), CHAT_ID));
        assertThat(jpaSubscriptionRepository.findAllByLinkId(linkDto.getId())).containsExactly(expected);
    }
}
