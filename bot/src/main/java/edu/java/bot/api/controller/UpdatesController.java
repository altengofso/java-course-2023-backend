package edu.java.bot.api.controller;

import edu.java.bot.api.models.LinkUpdate;
import edu.java.bot.api.service.UpdatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UpdatesController implements UpdatesApi {
    private final UpdatesService updatesService;

    @Override
    public ResponseEntity<Void> sendUpdates(LinkUpdate linkUpdate) {
        log.info("Sending update {}", linkUpdate);
        updatesService.sendUpdates(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
