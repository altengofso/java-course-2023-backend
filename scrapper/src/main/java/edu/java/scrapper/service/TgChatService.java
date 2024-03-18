package edu.java.scrapper.service;

public interface TgChatService {
    void registerChat(long chatId);

    void deleteChat(long chatId);

    boolean verifyChatExistence(long chatId);
}
