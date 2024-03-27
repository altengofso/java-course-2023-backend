package edu.java.scrapper.service;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.controller.dto.ChatResponse;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.chat.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {"app.database-access-type=jdbc"})
public class JdbcTgChatServiceTest extends IntegrationEnvironment {
    private static final long CHAT_ID = 1L;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private TgChatService tgChatService;

    @Test
    @Transactional
    @Rollback
    void getChatTest() {
        ChatDto expected = jdbcChatRepository.add(CHAT_ID);
        ChatResponse actual = tgChatService.getChat(CHAT_ID);
        assertThat(actual.id()).isEqualTo(expected.getId());
    }

    @Test
    @Transactional
    @Rollback
    void getChatShouldThrowNotFoundTest() {
        assertThrows(NotFoundException.class, () -> tgChatService.getChat(CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    void registerChatTest() {
        ChatResponse expected = tgChatService.registerChat(CHAT_ID);
        ChatResponse actual = tgChatService.getChat(CHAT_ID);
        assertThat(actual.id()).isEqualTo(expected.id());
    }

    @Test
    @Transactional
    @Rollback
    void registerChatShouldThrowConflictTest() {
        tgChatService.registerChat(CHAT_ID);
        assertThrows(ConflictException.class, () -> tgChatService.registerChat(CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        tgChatService.registerChat(CHAT_ID);
        tgChatService.deleteChat(CHAT_ID);
        assertThrows(NotFoundException.class, () -> tgChatService.getChat(CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatShouldThrowNotFoundTest() {
        assertThrows(NotFoundException.class, () -> tgChatService.deleteChat(CHAT_ID));
    }

    @Test
    @Transactional
    @Rollback
    void verifyChatExistenceTest() {
        tgChatService.registerChat(CHAT_ID);
        boolean expected = true;
        boolean actual = tgChatService.verifyChatExistence(CHAT_ID);
        assertThat(actual).isEqualTo(expected);
    }
}
