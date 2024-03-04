package edu.java.scrapper.api;

import edu.java.scrapper.api.controller.TgChatController;
import edu.java.scrapper.api.service.TgChatService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TgChatController.class)
public class TgChatControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TgChatService tgChatService;

    private static final long CHAT_ID = 1L;

    @Test
    @SneakyThrows
    void testRegisterChatShouldReturnOKWhenNotRegisteredYet() {
        Mockito.doReturn(false).when(tgChatService).findById(CHAT_ID);

        mvc.perform(post("/tg-chat/" + CHAT_ID)).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testRegisterChatShouldReturnConflictWhenAlreadyRegistered() {
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);

        mvc.perform(post("/tg-chat/" + CHAT_ID)).andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void testDeleteChatShouldReturnOKWhenRegisteredChatGiven() {
        Mockito.doReturn(true).when(tgChatService).findById(CHAT_ID);

        mvc.perform(delete("/tg-chat/" + CHAT_ID)).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testDeleteChatShouldReturnNotFoundWhenNotRegisteredChatGiven() {
        Mockito.doReturn(false).when(tgChatService).findById(CHAT_ID);

        mvc.perform(delete("/tg-chat/" + CHAT_ID)).andExpect(status().isNotFound());
    }
}
