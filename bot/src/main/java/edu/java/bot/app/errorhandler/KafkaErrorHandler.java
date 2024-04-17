package edu.java.bot.app.errorhandler;

import edu.java.bot.app.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class KafkaErrorHandler implements CommonErrorHandler {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApplicationConfig applicationConfig;

    @Override
    public boolean handleOne(
        Exception thrownException,
        ConsumerRecord<?, ?> consumerRecord,
        Consumer<?, ?> consumer,
        MessageListenerContainer container
    ) {
        handle(thrownException, consumer);
        return true;
    }

    @Override
    public void handleOtherException(
        Exception thrownException,
        Consumer<?, ?> consumer,
        MessageListenerContainer container,
        boolean batchListener
    ) {
        handle(thrownException, consumer);
    }

    private void handle(Exception exception, Consumer<?, ?> consumer) {
        log.error("Exception thrown", exception);
        if (exception instanceof RecordDeserializationException ex) {
            String rootCause = ExceptionUtils.getRootCauseMessage(exception);
            log.error("Send to DLQ: {}", rootCause);
            kafkaTemplate.send(
                applicationConfig.deadLetterQueueTopic().name(),
                rootCause
            );
            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        } else {
            log.error("Exception not handled", exception);
        }
    }
}
