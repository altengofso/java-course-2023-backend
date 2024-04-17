package edu.java.bot.app;

import edu.java.bot.app.configuration.ApplicationConfig;
import edu.java.bot.app.errorhandler.KafkaErrorHandler;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KafkaErrorHandlerTest {
    @Test
    public void testHandleOneRecordDeserializationException() {
        KafkaTemplate<String, String> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        ApplicationConfig applicationConfig = Mockito.mock(ApplicationConfig.class);
        ConsumerRecord<String, String> record = Mockito.mock(ConsumerRecord.class);
        Consumer<String, String> consumer = Mockito.mock(Consumer.class);
        MessageListenerContainer container = Mockito.mock(MessageListenerContainer.class);

        Mockito.when(applicationConfig.deadLetterQueueTopic())
            .thenReturn(new ApplicationConfig.Topic("dead-letter-queue", 1, 1));

        RecordDeserializationException ex =
            new RecordDeserializationException(new TopicPartition("testTopic", 0), 10L, "Test exception", null);

        KafkaErrorHandler errorHandler = new KafkaErrorHandler(kafkaTemplate, applicationConfig);

        errorHandler.handleOne(ex, record, consumer, container);

        verify(kafkaTemplate, times(1)).send(eq(applicationConfig.deadLetterQueueTopic().name()), any(String.class));
        verify(consumer, times(1)).seek(any(TopicPartition.class), eq(11L));
        verify(consumer, times(1)).commitSync();
    }

    @Test
    public void testHandleOtherExceptionGenericException() {
        KafkaTemplate<String, String> kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        ApplicationConfig applicationConfig = Mockito.mock(ApplicationConfig.class);
        Consumer<String, String> consumer = Mockito.mock(Consumer.class);
        MessageListenerContainer container = Mockito.mock(MessageListenerContainer.class);

        Exception genericEx = new Exception("Generic exception");

        KafkaErrorHandler errorHandler = new KafkaErrorHandler(kafkaTemplate, applicationConfig);

        errorHandler.handleOtherException(genericEx, consumer, container, false);

        verify(kafkaTemplate, times(0)).send(any(String.class), any(String.class));
        verify(consumer, times(0)).seek(any(TopicPartition.class), anyLong());
        verify(consumer, times(0)).commitSync();
    }
}
