package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.app.models.user.User;
import edu.java.bot.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("list")
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "показать список отслеживаемых ссылок";
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
    public SendMessage handle(long id) {
        User user = userRepository.findById(id);
        if (user.getLinks().isEmpty()) {
            return new SendMessage(id, NO_LINKS);
        }
        return new SendMessage(id, user.getLinksList());
    }
}
