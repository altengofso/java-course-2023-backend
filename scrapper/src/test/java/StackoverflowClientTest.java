import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.client.StackoverflowClient;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8080)
public class StackoverflowClientTest {
    @Test
    void testStackoverflowClientReturnsExpectedOffsetDateTime() {
        OffsetDateTime expected = OffsetDateTime.ofInstant(Instant.ofEpochSecond(1708873503), ZoneOffset.UTC);
        String baseUrl = "http://localhost:8080";
        String path = "/questions/78056447?site=stackoverflow";
        StackoverflowClient client = new StackoverflowClient(baseUrl);
        WireMock.stubFor(
            WireMock.get(path)
                .willReturn(
                    WireMock.okJson("{\"items\":[{\"last_activity_date\":1708873503}]}")
                )
        );
        OffsetDateTime actual = client.getUpdatedAt(baseUrl + path);
        assertThat(actual).isEqualTo(expected);
    }
}
