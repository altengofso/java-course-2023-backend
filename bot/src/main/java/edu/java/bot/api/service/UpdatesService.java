package edu.java.bot.api.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.models.LinkUpdate;
import edu.java.bot.app.controller.BotController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatesService {
    private final BotController botController;

    public void sendUpdates(LinkUpdate linkUpdate) {
        for (long chatId : linkUpdate.tgChatIds()) {
            botController.sendMessage(
                new SendMessage(
                    chatId,
                    linkUpdate.description()
                )
            );
        }
    }
}
