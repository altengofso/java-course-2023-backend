package edu.java.scrapper.app;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.apiclient.GithubApiClient;
import edu.java.scrapper.client.dto.ClientResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
public class GitbubApiClientTest {
    @Test
    void testGithubApiClientReturnsExpectedResponse() {
        ClientResponse expected = new ClientResponse(OffsetDateTime.parse("2024-02-25T15:05:03Z"));
        String baseUrl = "http://localhost:8080";
        String responsePath = "/repos/sanyarnd/java-course-2023-backend-template";
        String requestPath = "/sanyarnd/java-course-2023-backend-template";
        GithubApiClient client = new GithubApiClient(baseUrl);
        WireMock.stubFor(
            WireMock.get(responsePath)
                .willReturn(
                    WireMock.okJson("{\"updated_at\":\"2024-02-25T15:05:03Z\"}")
                )
        );
        ClientResponse actual = client.getResponse(baseUrl + requestPath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testCanRespondMethodReturnTrueWhenValidLinkGiven() {
        String link = "https://github.com/sanyarnd/java-course-2023-backend-template";
        boolean expected = true;
        GithubApiClient client = new GithubApiClient();
        boolean actual = client.canAccess(link);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testCanRespondMethodReturnFalseWhenInvalidLinkGiven() {
        String link = "https://yandex.ru";
        boolean expected = false;
        GithubApiClient client = new GithubApiClient();
        boolean actual = client.canAccess(link);
        assertThat(actual).isEqualTo(expected);
    }
}
