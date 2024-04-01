package edu.java.scrapper.repository.chat.jpa;

import edu.java.scrapper.repository.dto.ChatDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<ChatDto, Long> {
}
