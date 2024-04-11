package edu.java.scrapper.service.sender;

import edu.java.scrapper.client.botclient.models.LinkUpdate;
import edu.java.scrapper.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaLinkUpdateSender implements LinkUpdateSenderService {
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;
    private final ApplicationConfig applicationConfig;

    @Override
    public void send(LinkUpdate update) {
        kafkaTemplate.send(applicationConfig.updatesTopic().name(), update);
    }
}
