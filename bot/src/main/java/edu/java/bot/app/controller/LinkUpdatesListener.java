package edu.java.bot.app.controller;

import edu.java.bot.api.models.LinkUpdate;
import edu.java.bot.api.service.UpdatesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class LinkUpdatesListener {
    private final UpdatesService updatesService;

    @KafkaListener(
        topics = "${app.updates-topic.name}",
        autoStartup = "true",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "main"
    )
    public void listen(LinkUpdate update) {
        log.info("KafkaListener received link update: " + update);
        updatesService.sendUpdates(update);
    }
}
