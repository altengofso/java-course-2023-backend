package edu.java.scrapper.service.sender;

import edu.java.scrapper.client.botclient.models.LinkUpdate;
import edu.java.scrapper.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;

@Log4j2
@RequiredArgsConstructor
public class KafkaLinkUpdateSender implements LinkUpdateSenderService {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig applicationConfig;

    @Override
    public void send(LinkUpdate update) {
        log.info("Sending link update to kafka topic: {}", update);
        kafkaTemplate.send(applicationConfig.updatesTopic().name(), update);
    }
}
