package com.gp.radioanalytics.kafka.consumer;

import com.gp.radioanalytics.department.service.DepartmentEventProcessingService;
import com.gp.radioanalytics.kafka.event.DepartmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import static com.gp.radioanalytics.constant.KafkaConstants.TOPIC_SUFFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepartmentEventListener {
    private final DepartmentEventProcessingService departmentEventProcessingService;
    private final JsonMapper jsonMapper;

    private static final String TOPIC_NAME = "department" + TOPIC_SUFFIX;

    @KafkaListener(topics = TOPIC_NAME, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String payload) {
        DepartmentEvent departmentEvent = jsonMapper.readValue(payload, DepartmentEvent.class);
        departmentEventProcessingService.process(departmentEvent);
    }
}
