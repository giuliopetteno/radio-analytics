package com.gp.radioanalytics.kafka.consumer;

import com.gp.radioanalytics.devicetype.service.DeviceTypeEventProcessingService;
import com.gp.radioanalytics.kafka.event.DeviceTypeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import static com.gp.radioanalytics.kafka.constants.KafkaConstants.TOPIC_SUFFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceTypeEventListener {
    private final DeviceTypeEventProcessingService deviceTypeEventProcessingService;
    private final JsonMapper jsonMapper;

    private static final String TOPIC_NAME = "device-type" + TOPIC_SUFFIX;

    @KafkaListener(topics = TOPIC_NAME, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String payload) {
        DeviceTypeEvent deviceTypeEvent = jsonMapper.readValue(payload, DeviceTypeEvent.class);
        deviceTypeEventProcessingService.process(deviceTypeEvent);
    }
}
