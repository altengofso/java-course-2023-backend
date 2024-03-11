package edu.java.scrapper.api.service;

import edu.java.scrapper.api.exceptions.ConflictException;
import edu.java.scrapper.api.exceptions.ExceptionMessage;
import edu.java.scrapper.api.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TgChatService {
    public void registerChat(long chatId) {
        if (verifyChatExistence(chatId)) {
            throw new ConflictException(ExceptionMessage.TGCHAT_CONFLICT_MESSAGE.formatted(chatId));
        }
    }

    public void deleteChat(long chatId) {
        if (!verifyChatExistence(chatId)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
    }

    public boolean verifyChatExistence(long chatId) {
        return false;
    }
}
