package com.gp.radioanalytics.kafka.consumer;

import com.gp.radioanalytics.device.service.DeviceEventProcessingService;
import com.gp.radioanalytics.kafka.event.DeviceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import static com.gp.radioanalytics.constant.KafkaConstants.TOPIC_SUFFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceEventListener {
    private final DeviceEventProcessingService deviceEventProcessingService;
    private final JsonMapper jsonMapper;

    private static final String TOPIC_NAME = "device" + TOPIC_SUFFIX;

    @KafkaListener(topics = TOPIC_NAME, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String payload) {
        DeviceEvent deviceEvent = jsonMapper.readValue(payload, DeviceEvent.class);
        deviceEventProcessingService.process(deviceEvent);
    }
}
