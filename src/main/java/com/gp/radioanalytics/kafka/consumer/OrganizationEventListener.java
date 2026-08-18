package com.gp.radioanalytics.kafka.consumer;

import com.gp.radioanalytics.kafka.event.OrganizationEvent;
import com.gp.radioanalytics.organization.service.OrganizationEventProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import static com.gp.radioanalytics.kafka.constants.KafkaConstants.TOPIC_SUFFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationEventListener {
    private final OrganizationEventProcessingService organizationEventProcessingService;
    private final JsonMapper jsonMapper;

    private static final String TOPIC_NAME = "organization" + TOPIC_SUFFIX;

    @KafkaListener(topics = TOPIC_NAME, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String payload) {
        OrganizationEvent organizationEvent = jsonMapper.readValue(payload, OrganizationEvent.class);
        organizationEventProcessingService.process(organizationEvent);
    }
}
