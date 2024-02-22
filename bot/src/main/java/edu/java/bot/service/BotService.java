package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.NonSlashCommand;
import edu.java.bot.command.UnregisteredCommand;
import edu.java.bot.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {
    private final Map<String, Command> commands;
    private final NonSlashCommand nonSlashCommand;
    private final UnregisteredCommand unregisteredCommand;
    private final UserRepository userRepository;

    public SendMessage getSendMessage(Update update) {
        long id = update.message().chat().id();
        String message = update.message().text();
        if (userRepository.findById(id) == null && !message.equals("/start")) {
            return handleUnregisteredCommand(id);
        }
        if (message.startsWith("/")) {
            Command command = commands.get(message.substring(1));
            if (command != null) {
                return command.handle(id);
            }
        }
        return handleNonSlashCommand(id, message);
    }

    private SendMessage handleNonSlashCommand(long id, String message) {
        return nonSlashCommand.handle(id, message);
    }

    private SendMessage handleUnregisteredCommand(long id) {
        return unregisteredCommand.handle(id);
    }
}
