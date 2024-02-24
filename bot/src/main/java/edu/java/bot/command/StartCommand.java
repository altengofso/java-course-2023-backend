package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.models.user.User;
import edu.java.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("start")
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "регистрация пользователя";
    private static final String SUCCESS = "Привет! Я помогу отслеживать изменения на GitHub и StackOverflow.";
    private static final String FAIL = "Уже зарегистрирован, можно начинать отслеживать ссылки";
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
        boolean exists = userRepository.findById(id) != null;
        if (exists) {
            return new SendMessage(id, FAIL);
        }
        userRepository.addUser(new User(id));
        return new SendMessage(id, SUCCESS);
    }
}
