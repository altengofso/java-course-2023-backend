import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.GithubClient;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
public class GitbubClientTest {
    @Test
    void testGithubClientReturnsExpectedOffsetDateTime() {
        OffsetDateTime expected = OffsetDateTime.parse("2024-02-25T15:05:03Z");
        String baseUrl = "http://localhost:8080";
        String responsePath = "/repos/sanyarnd/java-course-2023-backend-template";
        String requestPath = "/sanyarnd/java-course-2023-backend-template";
        GithubClient client = new GithubClient(baseUrl);
        WireMock.stubFor(
            WireMock.get(responsePath)
                .willReturn(
                    WireMock.okJson("{\"updated_at\":\"2024-02-25T15:05:03Z\"}")
                )
        );
        OffsetDateTime actual = client.getUpdatedAt(baseUrl + requestPath);
        assertThat(actual).isEqualTo(expected);
    }
}
