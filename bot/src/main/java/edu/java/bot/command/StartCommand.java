package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.User;
import edu.java.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/start")
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "регистрация пользователя";
    public static final String SUCCESS = "Привет! Я помогу отслеживать изменения на GitHub и StackOverflow.";
    public static final String FAIL = "Уже зарегистрирован, можно начинать отслеживать ссылки";
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
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        boolean exists = userRepository.findById(id) != null;
        if (exists) {
            return new SendMessage(id, FAIL);
        }
        userRepository.addUser(new User(update.message().chat().id()));
        return new SendMessage(id, SUCCESS);
    }
}
