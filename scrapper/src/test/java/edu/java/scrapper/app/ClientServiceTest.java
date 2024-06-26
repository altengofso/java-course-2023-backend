package edu.java.scrapper.app;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.ScrapperApplication;
import edu.java.scrapper.client.apiclient.ApiClient;
import edu.java.scrapper.client.apiclient.GithubApiClient;
import edu.java.scrapper.client.apiclient.StackoverflowApiClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.service.ClientService;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ClientServiceTest extends IntegrationEnvironment {
    @Autowired
    private ClientService clientService;

    private static Stream<Arguments> testArguments() {
        return Stream.of(
            Arguments.of(
                "https://github.com/sanyarnd/java-course-2023-backend-template",
                new GithubApiClient(null, null)
            ),
            Arguments.of(
                "https://stackoverflow.com/questions/78056447?site=stackoverflow",
                new StackoverflowApiClient(null, null)
            )
        );
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void testClientServiceReturnCorrectApiClient(String link, ApiClient client) {
        assertThat(clientService.getApiClient(link).getClass()).isEqualTo(client.getClass());
    }
}
