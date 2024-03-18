package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.ExceptionMessage;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.chat.jdbc.JdbcChatRepository;
import edu.java.scrapper.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public void registerChat(long chatId) {
        if (verifyChatExistence(chatId)) {
            throw new ConflictException(ExceptionMessage.TGCHAT_CONFLICT_MESSAGE.formatted(chatId));
        }
        chatRepository.add(chatId);
    }

    @Override
    public void deleteChat(long chatId) {
        if (!verifyChatExistence(chatId)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        chatRepository.remove(chatId);
    }

    @Override
    public boolean verifyChatExistence(long chatId) {
        return chatRepository.findById(chatId).isPresent();
    }
}
