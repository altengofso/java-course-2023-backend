import edu.java.bot.BotApplication;
import edu.java.bot.processor.URIProcessor;
import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {BotApplication.class})
public class URIProcessorTest {
    @Autowired
    private URIProcessor uriProcessor;

    private static Stream<Arguments> validLinks() {
        return Stream.of(
            Arguments.of(URI.create("https://github.com/")),
            Arguments.of(URI.create("https://github.com/altengofso/")),
            Arguments.of(URI.create("https://stackoverflow.com/")),
            Arguments.of(URI.create("https://stackoverflow.com/search?q=unsupported%20link"))
        );
    }

    private static Stream<Arguments> invalidLinks() {
        return Stream.of(
            Arguments.of(URI.create("https://ya.ru/")),
            Arguments.of(URI.create("https://google.com/"))
        );
    }

    @ParameterizedTest
    @MethodSource("validLinks")
    void testValidLinksValidationsReturnTrue(URI uri) {
        var actual = uriProcessor.isValidURI(uri);
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidLinks")
    void testInvalidLinksValidationsReturnFalse(URI uri) {
        var actual = uriProcessor.isValidURI(uri);
        assertThat(actual).isFalse();
    }
}
