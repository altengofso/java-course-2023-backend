package edu.java.scrapper.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.api.controller.LinksController;
import edu.java.scrapper.api.models.AddLinkRequest;
import edu.java.scrapper.api.models.LinkResponse;
import edu.java.scrapper.api.models.ListLinksResponse;
import edu.java.scrapper.api.models.RemoveLinkRequest;
import edu.java.scrapper.api.service.LinksService;
import edu.java.scrapper.api.service.TgChatService;
import java.net.URI;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinksController.class)
public class LinksControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private LinksService linksService;
    @MockBean
    private TgChatService tgChatService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final long CHAT_ID = 1L;

    @Test
    @SneakyThrows
    void testGetAllLinksShouldReturnCorrectResponse() {
        long linkId = 1L;
        URI url = new URI("https://github.com");
        ListLinksResponse listLinksResponse = new ListLinksResponse(List.of(new LinkResponse(linkId, url)), 1);
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);
        Mockito.doReturn(listLinksResponse)
            .when(linksService).getAllLinks(CHAT_ID);

        mvc.perform(get("/links").header("Tg-Chat-Id", CHAT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(linkId))
            .andExpect(jsonPath("$.links[0].url").value(url.toString()))
            .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @SneakyThrows
    void testGetAllLinksShouldReturnNotFoundWhenNotRegisteredChatGiven() {
        Mockito.doReturn(false).when(tgChatService).findById(CHAT_ID);

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
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);
        Mockito.doReturn(false).when(linksService).findById(CHAT_ID, url);
        Mockito.doReturn(linkResponse)
            .when(linksService).addLink(CHAT_ID, url);

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
        Mockito.doReturn(false).when(tgChatService).findById(CHAT_ID);
        Mockito.doReturn(false).when(linksService).findById(CHAT_ID, url);

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
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);
        Mockito.doReturn(true).when(linksService).findById(CHAT_ID, url);

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
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);
        Mockito.doReturn(true).when(linksService).findById(CHAT_ID, url);
        Mockito.doReturn(linkResponse).when(linksService).deleteLink(CHAT_ID, url);

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
        Mockito.doReturn(false).when(tgChatService).findById(CHAT_ID);

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
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);
        Mockito.doReturn(false).when(linksService).findById(CHAT_ID, url);

        mvc.perform(delete("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(removeLinkRequest)))
            .andExpect(status().isNotFound());
    }
}
