package edu.java.scrapper.repository.chat;

import edu.java.scrapper.repository.dto.ChatDto;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    ChatDto add(long chatId);

    ChatDto remove(long chatId);

    List<ChatDto> findAll();

    Optional<ChatDto> findById(long chatId);
}
