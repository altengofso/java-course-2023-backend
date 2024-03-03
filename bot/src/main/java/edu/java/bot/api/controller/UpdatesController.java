package edu.java.bot.api.controller;

import edu.java.bot.api.models.LinkUpdate;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatesController implements UpdatesApi {
    @Override
    public ResponseEntity<Void> sendUpdates(LinkUpdate linkUpdate) {
        throw new NotImplementedException("Метод не имеет реализации");
    }
}
