package edu.java.scrapper.service;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.ListLinkResponse;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.dto.LinkDto;
import edu.java.scrapper.repository.dto.SubscriptionDto;
import edu.java.scrapper.repository.dto.SubscriptionId;
import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.link.jpa.JpaLinkRepository;
import edu.java.scrapper.repository.subscription.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.repository.subscription.jpa.JpaSubscriptionRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {"app.database-access-type=jdbc"})
public class JdbcLinkServiceTest extends IntegrationEnvironment {
    private final static long CHAT_ID = 1L;
    private final static String URL = "https://github.com/altengofso/java-course-2023-backend";
    @Autowired
    private TgChatService tgChatService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcSubscriptionRepository jdbcSubscriptionRepository;

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void getAllLinksTest() {
        tgChatService.registerChat(CHAT_ID);
        LinkResponse linkResponse = linkService.addLink(CHAT_ID, new URI(URL));
        ListLinkResponse expected = new ListLinkResponse(List.of(linkResponse), 1);
        ListLinkResponse actual = linkService.getAllLinks(CHAT_ID);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @Transactional
    @Rollback
    void getAllLinksShouldThrowNotFoundTest() {
        assertThrows(NotFoundException.class, () -> linkService.getAllLinks(CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addLinkTest() {
        tgChatService.registerChat(CHAT_ID);
        LinkResponse expected = linkService.addLink(CHAT_ID, new URI(URL));
        LinkDto linkDto = jdbcLinkRepository.findByUrl(new URI(URL)).orElseThrow();
        SubscriptionDto subscriptionDto =
            jdbcSubscriptionRepository.findByLinkIdAndChatId(expected.id(), CHAT_ID).orElseThrow();
        assertThat(linkDto.getId()).isEqualTo(expected.id());
        assertThat(linkDto.getUrl()).isEqualTo(expected.url());
        assertThat(subscriptionDto.getLinkId()).isEqualTo(expected.id());
        assertThat(subscriptionDto.getChatId()).isEqualTo(CHAT_ID);
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addLinkShouldThrowNotFoundTest() {
        assertThrows(NotFoundException.class, () -> linkService.addLink(CHAT_ID, new URI(URL)));
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void addLinkShouldThrowConflictTest() {
        tgChatService.registerChat(CHAT_ID);
        linkService.addLink(CHAT_ID, new URI(URL));
        assertThrows(ConflictException.class, () -> linkService.addLink(CHAT_ID, new URI(URL)));
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void deleteLinkTest() {
        tgChatService.registerChat(CHAT_ID);
        linkService.addLink(CHAT_ID, new URI(URL));
        linkService.deleteLink(CHAT_ID, new URI(URL));
        assertThat(jdbcLinkRepository.findByUrl(new URI(URL))).isNotPresent();
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void deleteLinkShouldThrowNotFoundTest() {
        assertThrows(NotFoundException.class, () -> linkService.deleteLink(CHAT_ID, new URI(URL)));
    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    void verifyLinkExistanceTest() {
        tgChatService.registerChat(CHAT_ID);
        linkService.addLink(CHAT_ID, new URI(URL));
        boolean expected = true;
        boolean actual = linkService.verifyLinkExistance(CHAT_ID, new URI(URL));
        assertThat(actual).isEqualTo(expected);
    }
}
