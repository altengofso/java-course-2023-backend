package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.NullCommand;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private final Map<String, Command> commands;
    private final NullCommand nullCommand;

    public SendMessage getSendMessage(Update update) {
        String text = update.message().text();
        Command command = commands.get(text);
        if (command != null) {
            return command.handle(update);
        }
        return handleNullCommand(update);
    }

    private SendMessage handleNullCommand(Update update) {
        return nullCommand.handle(update);
    }
}
