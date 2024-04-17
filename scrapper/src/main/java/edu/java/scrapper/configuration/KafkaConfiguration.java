package edu.java.scrapper.configuration;

import edu.java.scrapper.client.botclient.models.LinkUpdate;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {
    @Bean
    public KafkaTemplate<String, LinkUpdate> kafkaTemplate(ProducerFactory<String, LinkUpdate> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic updatesTopic(ApplicationConfig applicationConfig) {
        return TopicBuilder.name(applicationConfig.updatesTopic().name())
            .replicas(applicationConfig.updatesTopic().replicas())
            .partitions(applicationConfig.updatesTopic().partitions())
            .build();
    }
}
