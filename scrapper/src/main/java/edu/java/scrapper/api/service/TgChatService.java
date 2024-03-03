package edu.java.scrapper.api.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
public class TgChatService {
    public void registerChat(long chatId) {
        throw new NotImplementedException("Регистрация не реализована");
    }

    public void deleteChat(long chatId) {
        throw new NotImplementedException("Удаление не реализовано");
    }

    public boolean findById(long chatId) {
        return true;
    }
}
