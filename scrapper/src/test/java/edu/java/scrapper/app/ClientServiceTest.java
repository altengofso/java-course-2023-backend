package edu.java.scrapper.app;

import edu.java.scrapper.ScrapperApplication;
import edu.java.scrapper.app.client.ApiClient;
import edu.java.scrapper.app.client.GithubApiClient;
import edu.java.scrapper.app.client.StackoverflowApiClient;
import edu.java.scrapper.app.service.ClientService;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ScrapperApplication.class})
public class ClientServiceTest {
    @Autowired
    private ClientService clientService;

    private static Stream<Arguments> testArguments() {
        return Stream.of(
            Arguments.of(
                "https://github.com/sanyarnd/java-course-2023-backend-template",
                new GithubApiClient()
            ),
            Arguments.of(
                "https://stackoverflow.com/questions/78056447?site=stackoverflow",
                new StackoverflowApiClient()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void testClientServiceReturnCorrectApiClient(String link, ApiClient client) {
        assertThat(clientService.getApiClient(link).getClass()).isEqualTo(client.getClass());
    }
}
