package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.app.models.user.User;
import edu.java.bot.app.models.user.UserState;
import edu.java.bot.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("untrack")
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "удалить ссылку из отслеживания";
    private static final String NO_LINKS = "нет отслеживаемых ссылок";

    private final UserRepository userRepository;

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public synchronized SendMessage handle(long id) {
        User user = userRepository.findById(id);
        if (user.getLinks().isEmpty()) {
            return new SendMessage(id, NO_LINKS);
        }
        user.setUserState(UserState.AWAITING_UNTRACK_LINK);
        return new SendMessage(id, user.getLinksList());
    }
}
