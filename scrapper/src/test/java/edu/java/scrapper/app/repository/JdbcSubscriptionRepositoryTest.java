package edu.java.scrapper.app.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.subscription.jdbc.JdbcSubscriptionRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JdbcSubscriptionRepositoryTest extends IntegrationEnvironment {
    private static final String URL = "http://example.com";
    private static final long CHAT_ID = 1L;

    @Autowired
    private JdbcSubscriptionRepository jdbcSubscriptionRepository;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addTest() {
        LinkDto linkDto = jdbcClient.sql("insert into link (url) values (?) returning *")
            .param(URL)
            .query(LinkDto.class)
            .single();
        jdbcClient.sql("insert into chat (id) values (?)")
            .param(CHAT_ID)
            .update();
        jdbcSubscriptionRepository.add(linkDto.getId(), CHAT_ID);
        assertThat(jdbcSubscriptionRepository.findByLinkIdAndChatId(linkDto.getId(), CHAT_ID)).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void removeTest() {
        LinkDto linkDto = jdbcClient.sql("insert into link (url) values (?) returning *")
            .param(URL)
            .query(LinkDto.class)
            .single();
        jdbcClient.sql("insert into chat (id) values (?)")
            .param(CHAT_ID)
            .update();
        jdbcSubscriptionRepository.add(linkDto.getId(), CHAT_ID);
        jdbcSubscriptionRepository.remove(linkDto.getId(), CHAT_ID);
        assertThat(jdbcSubscriptionRepository.findByLinkIdAndChatId(linkDto.getId(), CHAT_ID)).isNotPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findByLinkIdAndChatIdTest() {
        LinkDto linkDto = jdbcClient.sql("insert into link (url) values(?) returning *")
            .param(URL)
            .query(LinkDto.class)
            .single();
        jdbcClient.sql("insert into chat (id) values (?)")
            .param(CHAT_ID)
            .update();
        SubscriptionDto expected = jdbcSubscriptionRepository.add(linkDto.getId(), CHAT_ID);
        SubscriptionDto actual = jdbcSubscriptionRepository.findByLinkIdAndChatId(linkDto.getId(), CHAT_ID).orElseThrow();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void findAllByLinkIdTesT() {
        LinkDto linkDto = jdbcClient.sql("insert into link (url) values(?) returning *")
            .param(URL)
            .query(LinkDto.class)
            .single();
        jdbcClient.sql("insert into chat (id) values (?)")
            .param(CHAT_ID)
            .update();
        SubscriptionDto expected = jdbcSubscriptionRepository.add(linkDto.getId(), CHAT_ID);
        assertThat(jdbcSubscriptionRepository.findAllByLinkId(linkDto.getId())).containsExactly(expected);
    }
}
