import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.command.Command;
import edu.java.bot.command.NonSlashCommand;
import edu.java.bot.command.UnregisteredCommand;
import edu.java.bot.models.user.User;
import edu.java.bot.models.user.UserState;
import edu.java.bot.repository.UserRepository;
import edu.java.bot.service.LinkService;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.experimental.runners.Enclosed;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
@SpringBootTest(classes = {BotApplication.class})
public class CommandTest {
    @MockBean
    private Update update;

    @Autowired
    private Map<String, Command> commands;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkService linkService;

    @Nested
    class StartCommandTest {
        private final Command start = commands.get("start");

        @Test
        void testStartCommandUserRegistration() {
            long id = 1L;
            assertThat(userRepository.findById(id)).isNull();

            prepareMock(id);
            start.handle(id);

            User actual = userRepository.findById(id);
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(id);
        }

        @Test
        void testStartCommandReturnCorrectMessageWhenRepeatedStartCommandSend() {
            long id = 1L;
            start.handle(id);
            String expected = "Уже зарегистрирован, можно начинать отслеживать ссылки";
            String actual = start.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class HelpCommandTest {
        private final Command help = commands.get("help");

        @Test
        void testHelpCommandReturnCorrectDescription() {
            String expected = """
                /list - показать список отслеживаемых ссылок
                /start - регистрация пользователя
                /track - добавить ссылку в отслеживание
                /untrack - удалить ссылку из отслеживания""";

            long id = 2L;
            prepareUser(id, Set.of());
            prepareMock(id);
            String actual = help.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class ListCommandTest {
        private final Command list = commands.get("list");

        @Test
        @SneakyThrows
        void testListCommandReturnTrackedLinksList() {
            long id = 3L;
            prepareUser(id, Set.of(new URI("https://github.com"), new URI("https://stackoverflow.com")));
            prepareMock(id);
            String expected = """
                https://github.com
                https://stackoverflow.com""";
            String actual = list.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testListCommandReturnCorrectMessageWhenNoLinksTracked() {
            long id = 4L;
            prepareUser(id, Set.of());
            prepareMock(id);
            String expected = "нет отслеживаемых ссылок";
            String actual = list.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class UnregisteredCommandTest {
        private final UnregisteredCommand unregistered = new UnregisteredCommand();

        @Test
        void testUnregisteredCommandReturnCorrectMessageWhenUserNotRegistered() {
            long id = 5L;
            prepareMock(id);
            String expected = "необходимо зарегистрироваться /start";
            String actual = unregistered.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class TrackCommandTest {
        private final Command track = commands.get("track");

        @Test
        void testTrackCommandSetCorrectUserState() {
            long id = 6L;
            prepareUser(id, Set.of());
            prepareMock(id);
            track.handle(id);
            UserState expected = UserState.AWAITING_TRACK_LINK;
            UserState actual = userRepository.findById(id).getUserState();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testTrackCommandReturnCorrectMessage() {
            long id = 7L;
            prepareUser(id, Set.of());
            prepareMock(id);
            String expected = "введите ссылку для отслеживания";
            String actual = track.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class UntrackCommandTest {
        private final Command untrack = commands.get("untrack");

        @Test
        @SneakyThrows
        void testUntrackCommandSetCorrectUserStateWhenTrackedLinksExists() {
            long id = 8L;
            prepareUser(id, Set.of(new URI("https://github.com"), new URI("https://stackoverflow.com")));
            prepareMock(id);
            untrack.handle(id);
            UserState expected = UserState.AWAITING_UNTRACK_LINK;
            UserState actual = userRepository.findById(id).getUserState();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @SneakyThrows
        void testUntrackCommandNotChangeUserStateWhenNoLinksTracked() {
            long id = 9L;
            prepareUser(id, Set.of());
            prepareMock(id);
            untrack.handle(id);
            UserState expected = UserState.REGULAR;
            UserState actual = userRepository.findById(id).getUserState();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @SneakyThrows
        void testUntrackCommandReturnCorrectMessageWhenTrackedLinksExists() {
            long id = 10L;
            prepareUser(id, Set.of(new URI("https://github.com"), new URI("https://stackoverflow.com")));
            prepareMock(id);
            String expected = """
                https://github.com
                https://stackoverflow.com""";
            String actual = untrack.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testUntrackCommandReturnCorrectMessageWhenNoLinksTracked() {
            long id = 11L;
            prepareUser(id, Set.of());
            prepareMock(id);
            String expected = "нет отслеживаемых ссылок";
            String actual = untrack.handle(id).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class NonSlashCommandTest {
        private final NonSlashCommand nonSlashCommand = new NonSlashCommand(userRepository, linkService);

        @Test
        void testNonSlashCommandReturnCorrectMessageWhenNotAwaitingTrackOrUntrackLink() {
            long id = 12L;
            prepareUser(id, Set.of());
            prepareMock(id);
            String expected = "неподдерживаемая команда";
            String actual = nonSlashCommand.handle(id, "/hello").getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testNonSlashCommandReturnCorrectMessageWhenSupportedLinkSentToTrack() {
            Command track = commands.get("track");
            String link = "https://github.com";
            long id = 13L;
            prepareUser(id, Set.of());
            prepareMock(id);
            track.handle(id);
            String expected = "ссылка успешно добавлена в отслеживание";
            String actual = nonSlashCommand.handle(id, link).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void testNonSlashCommandReturnCorrectMessageWhenUnsupportedLinkSentToTrack() {
            Command track = commands.get("track");
            String link = "https://yandex.ru";
            long id = 14L;
            prepareUser(id, Set.of());
            prepareMock(id);
            track.handle(id);
            String expected = "произошла ошибка, ссылка не поддерживается";
            String actual = nonSlashCommand.handle(id, link).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @SneakyThrows
        void testNonSlashCommandReturnCorrectMessageWhenSupportedLinkSentToUntrack() {
            Command untrack = commands.get("untrack");
            String link = "https://github.com";
            long id = 15L;
            prepareUser(id, Set.of(new URI("https://github.com")));
            prepareMock(id);
            untrack.handle(id);
            String expected = "ссылка успешно удалена из отслеживания";
            String actual = nonSlashCommand.handle(id, link).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @SneakyThrows
        void testNonSlashCommandReturnCorrectMessageWhenUnsupportedLinkSentToUntrack() {
            Command untrack = commands.get("untrack");
            String link = "https://yandex.ru";
            long id = 16L;
            prepareUser(id, Set.of(new URI("https://github.com")));
            prepareMock(id);
            untrack.handle(id);
            String expected = "произошла ошибка, ссылка не поддерживается";
            String actual = nonSlashCommand.handle(id, link).getParameters().get("text").toString();
            assertThat(actual).isEqualTo(expected);
        }
    }

    private void prepareUser(long id, Set<URI> links) {
        User user = new User(id);
        user.getLinks().addAll(links);
        userRepository.addUser(user);
    }

    private void prepareMock(long id) {
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(id);
        Mockito.when(update.message().chat().id()).thenReturn(id);
    }
}
