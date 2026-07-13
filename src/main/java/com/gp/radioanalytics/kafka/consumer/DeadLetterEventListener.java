package com.gp.radioanalytics.kafka.consumer;

import com.gp.radioanalytics.kafka.deadletterevent.domain.DeadLetterEvent;
import com.gp.radioanalytics.kafka.deadletterevent.repository.DeadLetterEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.gp.radioanalytics.constant.KafkaConstants.DLT_SUFFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadLetterEventListener {
    private final DeadLetterEventRepository deadLetterEventRepository;

    @KafkaListener(topics = "#{'${kafka.consumer.dlt.topics}'.split(',')}", groupId = "${spring.kafka.consumer.group-id}-dlt")
    public void handleDeadLetter(
            @Payload String payload,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String dltTopic,
            @Header(name = KafkaHeaders.ORIGINAL_TOPIC, required = false) byte[] originalTopic,
            @Header(name = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String exceptionMessage,
            @Header(name = KafkaHeaders.EXCEPTION_FQCN, required = false) String exceptionClass) {

        String resolvedOriginalTopic = originalTopic != null
                ? new String(originalTopic)
                : dltTopic.substring(0, dltTopic.length() - DLT_SUFFIX.length());

        DeadLetterEvent event = DeadLetterEvent.builder()
                .originalTopic(resolvedOriginalTopic)
                .payload(payload)
                .errorMessage(exceptionMessage)
                .errorClass(exceptionClass)
                .build();

        deadLetterEventRepository.save(event);
        log.warn("Persisted dead letter event from topic {}", resolvedOriginalTopic);
    }
}
