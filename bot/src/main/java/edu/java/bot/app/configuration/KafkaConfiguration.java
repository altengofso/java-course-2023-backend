package edu.java.bot.app.configuration;

import edu.java.bot.api.models.LinkUpdate;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;

@Configuration
public class KafkaConfiguration {
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdate> kafkaListenerContainerFactory(
        ConsumerFactory<String, LinkUpdate> consumerFactory,
        CommonErrorHandler commonErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdate> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(commonErrorHandler);
        factory.setConcurrency(1);
        return factory;
    }

    @Bean
    public NewTopic deadLetterQueueTopic(ApplicationConfig applicationConfig) {
        return TopicBuilder.name(applicationConfig.deadLetterQueueTopic().name())
            .replicas(applicationConfig.deadLetterQueueTopic().replicas())
            .partitions(applicationConfig.deadLetterQueueTopic().partitions())
            .build();
    }
}
