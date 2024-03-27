package edu.java.scrapper.service;

import edu.java.scrapper.controller.dto.ChatResponse;

public interface TgChatService {
    ChatResponse getChat(long chatId);

    ChatResponse registerChat(long chatId);

    ChatResponse deleteChat(long chatId);

    boolean verifyChatExistence(long chatId);
}
