package edu.java.bot.app.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.app.command.Command;
import edu.java.bot.app.command.NonSlashCommand;
import edu.java.bot.app.command.UnregisteredCommand;
import edu.java.bot.scrapperclient.ScrapperApiClient;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BotService {
    private final Map<String, Command> commands;
    private final NonSlashCommand nonSlashCommand;
    private final UnregisteredCommand unregisteredCommand;
    private final ScrapperApiClient scrapperApiClient;
    private final Counter messageCounter;

    public BotService(
        Map<String, Command> commands,
        NonSlashCommand nonSlashCommand,
        UnregisteredCommand unregisteredCommand,
        ScrapperApiClient scrapperApiClient,
        MeterRegistry meterRegistry
    ) {
        this.commands = commands;
        this.nonSlashCommand = nonSlashCommand;
        this.unregisteredCommand = unregisteredCommand;
        this.scrapperApiClient = scrapperApiClient;
        this.messageCounter = meterRegistry.counter("messages_processed_count");

    }

    public SendMessage getSendMessage(Update update) {
        messageCounter.increment();
        long id = update.message().chat().id();
        String message = update.message().text();
        if (scrapperApiClient.getChat(id) == null && !message.equals("/start")) {
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
