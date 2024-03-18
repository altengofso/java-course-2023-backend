package edu.java.scrapper.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.controller.LinkController;
import edu.java.scrapper.controller.dto.AddLinkRequest;
import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.ListLinkResponse;
import edu.java.scrapper.controller.dto.RemoveLinkRequest;
import edu.java.scrapper.repository.link.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.subscription.jdbc.JdbcSubscriptionRepository;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import edu.java.scrapper.service.jdbc.JdbcTgChatService;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
public class LinkControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private JdbcTgChatService tgChatService;
    @MockBean
    private JdbcLinkRepository jdbcLinkRepository;
    @MockBean
    private JdbcSubscriptionRepository jdbcSubscriptionRepository;
    @SpyBean
    private JdbcLinkService linkService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final long CHAT_ID = 1L;

    @Test
    @SneakyThrows
    void testGetAllLinksShouldReturnCorrectResponse() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        ListLinkResponse listLinkResponse = new ListLinkResponse(List.of(new LinkResponse(linkId, url)), 1);
        Mockito.doReturn(true).when(tgChatService).verifyChatExistence(CHAT_ID);
        Mockito.doReturn(listLinkResponse)
            .when(linkService).getAllLinks(CHAT_ID);

        mvc.perform(get("/links").header("Tg-Chat-Id", CHAT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(linkId))
            .andExpect(jsonPath("$.links[0].url").value(url.toString()))
            .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @SneakyThrows
    void testGetAllLinksShouldReturnNotFoundWhenNotRegisteredChatGiven() {
        Mockito.doReturn(false).when(tgChatService).verifyChatExistence(CHAT_ID);

        mvc.perform(get("/links").header("Tg-Chat-Id", CHAT_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void testAddLinkShouldReturnCorrectResponse() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        LinkResponse linkResponse = new LinkResponse(linkId, url);
        Mockito.doReturn(true).when(tgChatService).verifyChatExistence(CHAT_ID);
        Mockito.doReturn(false).when(linkService).verifyLinkExistance(CHAT_ID, url);
        Mockito.doReturn(linkResponse)
            .when(linkService).addLink(CHAT_ID, url);

        mvc.perform(post("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(addLinkRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(url.toString()));
    }

    @Test
    @SneakyThrows
    void testAddLinkShouldReturnNotFoundWhenNotRegisteredChatGiven() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        Mockito.doReturn(false).when(tgChatService).verifyChatExistence(CHAT_ID);
        Mockito.doReturn(false).when(linkService).verifyLinkExistance(CHAT_ID, url);

        mvc.perform(post("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(addLinkRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void testAddLinkShouldReturnConflictWhenLinkAlreadyAdded() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        Mockito.doReturn(true).when(tgChatService).verifyChatExistence(CHAT_ID);
        Mockito.doReturn(true).when(linkService).verifyLinkExistance(CHAT_ID, url);

        mvc.perform(post("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(addLinkRequest)))
            .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void testDeleteLinkShouldReturnCorrectResponse() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
        LinkResponse linkResponse = new LinkResponse(linkId, url);
        Mockito.doReturn(true).when(tgChatService).verifyChatExistence(CHAT_ID);
        Mockito.doReturn(true).when(linkService).verifyLinkExistance(CHAT_ID, url);
        Mockito.doReturn(linkResponse).when(linkService).deleteLink(CHAT_ID, url);

        mvc.perform(delete("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeLinkRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(url.toString()));
    }

    @Test
    @SneakyThrows
    void testDeleteLinkShouldReturnNotFoundWhenNotRegisteredChatGiven() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
        Mockito.doReturn(false).when(tgChatService).verifyChatExistence(CHAT_ID);

        mvc.perform(delete("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeLinkRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void testDeleteLinkShouldReturnNotFoundWhenNotAddedLinkGiven() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
        Mockito.doReturn(true).when(tgChatService).verifyChatExistence(CHAT_ID);
        Mockito.doReturn(false).when(linkService).verifyLinkExistance(CHAT_ID, url);

        mvc.perform(delete("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeLinkRequest)))
            .andExpect(status().isNotFound());
    }
}
