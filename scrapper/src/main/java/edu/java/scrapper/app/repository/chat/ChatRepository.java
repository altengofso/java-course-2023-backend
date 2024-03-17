package edu.java.scrapper.app.repository.chat;

import edu.java.scrapper.app.repository.dto.ChatDto;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    void add(long chatId);

    void remove(long chatId);

    List<ChatDto> findAll();

    Optional<ChatDto> findById(long chatId);
}
