package edu.java.scrapper.api.controller;

import edu.java.scrapper.api.exceptions.ConflictException;
import edu.java.scrapper.api.exceptions.ExceptionMessage;
import edu.java.scrapper.api.exceptions.NotFoundException;
import edu.java.scrapper.api.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TgChatController implements TgChatApi {
    private final TgChatService tgChatService;

    @Override
    public ResponseEntity<Void> registerChat(long id) {
        if (tgChatService.findById(id)) {
            throw new ConflictException(ExceptionMessage.TGCHAT_CONFLICT_MESSAGE.formatted(id));
        }
        tgChatService.registerChat(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteChat(long id) {
        if (!tgChatService.findById(id)) {
            throw new NotFoundException(ExceptionMessage.TGCHAT_NOTFOUND_MESSAGE);
        }
        tgChatService.deleteChat(id);
        return ResponseEntity.ok().build();
    }
}
