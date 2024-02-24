package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("help")
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private static final String COMMAND = "/help";
    private static final String DESCRIPTION = "доступные команды";
    private final Map<String, Command> commands;

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
        return new SendMessage(
            id,
            commands
                .values()
                .stream()
                .map(command -> command.command() + " - " + command.description())
                .collect(Collectors.joining("\n"))
        );
    }
}
