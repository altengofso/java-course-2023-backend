package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("/track")
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "добавить ссылку в отслеживание";
    private final LinkService linkService;

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
        String result = linkService.trackLink(update);
        return new SendMessage(update.message().chat().id(), result);
    }
}
