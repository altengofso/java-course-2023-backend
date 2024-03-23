package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.scrapperclient.ScrapperApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("start")
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String COMMAND = "/start";
    private static final String DESCRIPTION = "регистрация пользователя";
    private static final String SUCCESS = "Привет! Я помогу отслеживать изменения на GitHub и StackOverflow.";
    private static final String FAIL = "Уже зарегистрирован, можно начинать отслеживать ссылки";

    private final ScrapperApiClient scrapperApiClient;

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
        var chat = scrapperApiClient.getChat(id);
        if (chat != null) {
            return new SendMessage(id, FAIL);
        }
        scrapperApiClient.registerChat(id);
        return new SendMessage(id, SUCCESS);
    }
}
