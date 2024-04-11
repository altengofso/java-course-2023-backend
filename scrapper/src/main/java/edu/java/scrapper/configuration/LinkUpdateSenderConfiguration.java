package edu.java.scrapper.configuration;

import edu.java.scrapper.client.botclient.BotApiClient;
import edu.java.scrapper.client.botclient.models.LinkUpdate;
import edu.java.scrapper.service.sender.HttpLinkUpdateSender;
import edu.java.scrapper.service.sender.KafkaLinkUpdateSender;
import edu.java.scrapper.service.sender.LinkUpdateSenderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class LinkUpdateSenderConfiguration {

    @Bean
    public LinkUpdateSenderService linkUpdateSenderService(
        ApplicationConfig applicationConfig,
        KafkaTemplate<String, LinkUpdate> kafkaTemplate,
        BotApiClient botApiClient
    ) {
        if (applicationConfig.useQueue()) {
            return new KafkaLinkUpdateSender(kafkaTemplate, applicationConfig);
        }
        return new HttpLinkUpdateSender(botApiClient);
    }
}
