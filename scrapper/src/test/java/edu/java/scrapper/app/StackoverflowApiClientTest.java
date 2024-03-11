package edu.java.scrapper.app;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.app.client.StackoverflowApiClient;
import edu.java.scrapper.app.client.dto.ClientResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
public class StackoverflowApiClientTest {
    @Test
    void testStackoverflowApiClientReturnsExpectedResponse() {
        ClientResponse expected =
            new ClientResponse(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1708873503), ZoneOffset.UTC));
        String baseUrl = "http://localhost:8080";
        String path = "/questions/78056447?site=stackoverflow";
        StackoverflowApiClient client = new StackoverflowApiClient(baseUrl);
        WireMock.stubFor(
            WireMock.get(path)
                .willReturn(
                    WireMock.okJson("{\"items\":[{\"last_activity_date\":1708873503}]}")
                )
        );
        ClientResponse actual = client.getResponse(baseUrl + path);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testCanRespondMethodReturnTrueWhenValidLinkGiven() {
        String link = "https://stackoverflow.com/questions/78056447?site=stackoverflow";
        boolean expected = true;
        StackoverflowApiClient client = new StackoverflowApiClient();
        boolean actual = client.canAccess(link);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testCanRespondMethodReturnFalseWhenInvalidLinkGiven() {
        String link = "https://yandex.ru";
        boolean expected = false;
        StackoverflowApiClient client = new StackoverflowApiClient();
        boolean actual = client.canAccess(link);
        assertThat(actual).isEqualTo(expected);
    }
}
