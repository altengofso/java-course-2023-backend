package edu.java.bot.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.api.models.LinkUpdate;
import edu.java.bot.api.service.UpdatesService;
import edu.java.bot.app.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class LinkUpdatesListener {
    private final ObjectMapper objectMapper;
    private final UpdatesService updatesService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApplicationConfig applicationConfig;

    @KafkaListener(
        topics = "${app.updates-topic.name}",
        autoStartup = "true",
        containerFactory = "kafkaListenerContainerFactory",
        groupId = "main"
    )
    public void listen(String payload) {
        log.info("KafkaListener received payload: " + payload);
        try {
            LinkUpdate linkUpdate = objectMapper.readValue(payload, LinkUpdate.class);
            updatesService.sendUpdates(linkUpdate);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON LinkUpdate", e);
            kafkaTemplate.send(applicationConfig.deadLetterQueueTopic().name(), payload);
        }
    }
}
