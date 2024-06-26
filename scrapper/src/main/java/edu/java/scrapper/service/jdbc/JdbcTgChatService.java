package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.controller.dto.ChatResponse;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.ExceptionMessage;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.chat.jdbc.JdbcChatRepository;
import edu.java.scrapper.service.TgChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public ChatResponse getChat(long chatId) {
        var chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        return new ChatResponse(chat.get().getId(), chat.get().getCreatedAt());
    }

    @Override
    public ChatResponse registerChat(long chatId) {
        if (verifyChatExistence(chatId)) {
            throw new ConflictException(ExceptionMessage.TGCHAT_CONFLICT_MESSAGE.formatted(chatId));
        }
        var chat = chatRepository.add(chatId);
        return new ChatResponse(chat.getId(), chat.getCreatedAt());
    }

    @Override
    public ChatResponse deleteChat(long chatId) {
        if (!verifyChatExistence(chatId)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        var chat = chatRepository.remove(chatId);
        return new ChatResponse(chat.getId(), chat.getCreatedAt());
    }

    @Override
    public boolean verifyChatExistence(long chatId) {
        return chatRepository.findById(chatId).isPresent();
    }
}
