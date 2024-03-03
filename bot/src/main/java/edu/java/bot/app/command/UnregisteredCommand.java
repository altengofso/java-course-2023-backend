package edu.java.bot.app.command;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class UnregisteredCommand {
    private static final String FAIL = "необходимо зарегистрироваться /start";

    public SendMessage handle(long id) {
        return new SendMessage(id, FAIL);
    }
}
