package edu.java.bot.app.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.app.command.Command;
import edu.java.bot.app.service.BotService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;

@Controller
public class BotController implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final BotService botService;

    public BotController(TelegramBot telegramBot, BotService botService, Map<String, Command> commands) {
        telegramBot.setUpdatesListener(this);
        this.telegramBot = telegramBot;
        this.botService = botService;
        this.telegramBot.execute(createBotMenu(commands));
    }

    @Override
    public int process(List<Update> list) {
        if (!list.isEmpty()) {
            list.forEach(update -> {
                if (update.message() != null) {
                    telegramBot.execute(botService.getSendMessage(update));
                }
            });
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SetMyCommands createBotMenu(Map<String, Command> commands) {
        return new SetMyCommands(
            commands
                .values()
                .stream()
                .map(command -> new BotCommand(command.command(), command.description()))
                .toArray(BotCommand[]::new)
        );
    }
}
