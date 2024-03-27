package edu.java.scrapper.service.jpa;

import edu.java.scrapper.controller.dto.ChatResponse;
import edu.java.scrapper.controller.exceptions.ConflictException;
import edu.java.scrapper.controller.exceptions.ExceptionMessage;
import edu.java.scrapper.controller.exceptions.NotFoundException;
import edu.java.scrapper.repository.chat.jpa.JpaChatRepository;
import edu.java.scrapper.repository.dto.ChatDto;
import edu.java.scrapper.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaChatRepository chatRepository;

    @Override
    @Transactional
    public ChatResponse getChat(long chatId) {
        var chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        return new ChatResponse(chat.get().getId(), chat.get().getCreatedAt());
    }

    @Override
    @Transactional
    public ChatResponse registerChat(long chatId) {
        if (verifyChatExistence(chatId)) {
            throw new ConflictException(ExceptionMessage.TGCHAT_CONFLICT_MESSAGE.formatted(chatId));
        }
        var chat = chatRepository.save(new ChatDto(chatId));
        return new ChatResponse(chat.getId(), chat.getCreatedAt());
    }

    @Override
    @Transactional
    public ChatResponse deleteChat(long chatId) {
        var chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        chatRepository.delete(chat.get());
        return new ChatResponse(chat.get().getId(), chat.get().getCreatedAt());
    }

    @Override
    @Transactional
    public boolean verifyChatExistence(long chatId) {
        return chatRepository.existsById(chatId);
    }
}
