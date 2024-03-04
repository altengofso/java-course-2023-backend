package edu.java.bot.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.api.controller.UpdatesController;
import edu.java.bot.api.models.LinkUpdate;
import edu.java.bot.api.service.UpdatesService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdatesController.class)
public class UpdatesControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UpdatesService updatesService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void testSendUpdatesShouldReturnOK() {
        LinkUpdate linkUpdate = new LinkUpdate(1L, new URI("https://github.com"), "desc", List.of(1L));
        Mockito.doNothing().when(updatesService).sendUpdates(linkUpdate);

        mvc.perform(post("/updates")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(linkUpdate))
        ).andExpect(status().isOk());
    }
}
