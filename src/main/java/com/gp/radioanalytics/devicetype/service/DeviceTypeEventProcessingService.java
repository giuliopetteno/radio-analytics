package com.gp.radioanalytics.devicetype.service;

import com.gp.radioanalytics.devicetype.eventlog.domain.DeviceTypeEventLog;
import com.gp.radioanalytics.devicetype.snapshot.domain.DeviceTypeSnapshot;
import com.gp.radioanalytics.devicetype.eventlog.repository.DeviceTypeEventLogRepository;
import com.gp.radioanalytics.devicetype.snapshot.repository.DeviceTypeSnapshotRepository;
import com.gp.radioanalytics.enums.EntityType;
import com.gp.radioanalytics.enums.EventType;
import com.gp.radioanalytics.kafka.event.DeviceTypeEvent;
import com.gp.radioanalytics.kafka.processedevent.service.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceTypeEventProcessingService {
	private final ProcessedEventService processedEventService;
	private final DeviceTypeEventLogRepository deviceTypeEventLogRepository;
	private final DeviceTypeSnapshotRepository deviceTypeSnapshotRepository;

	@Transactional
	public void process(DeviceTypeEvent event) {
		if (processedEventService.isProcessed(event.eventId())) {
			log.info("Event {} already processed, skipping", event.eventId());
			return;
		}

		deviceTypeEventLogRepository.save(toEventLog(event));

		DeviceTypeSnapshot deviceTypeSnapshot = toSnapshot(event);
		if (event.eventType() == EventType.DELETE) {
			deviceTypeSnapshot.setDeleted(true);
			deviceTypeSnapshot.setDeletedAt(event.producedAt());
		}
		deviceTypeSnapshotRepository.save(deviceTypeSnapshot);

		processedEventService.markAsProcessed(event.eventId(), EntityType.DEVICE_TYPE.name());
		log.info("Event {} processed successfully", event.eventId());
	}

	private DeviceTypeEventLog toEventLog(DeviceTypeEvent event) {
		return DeviceTypeEventLog.builder()
			.eventId(event.eventId())
			.eventType(event.eventType().name())
			.deviceTypeId(event.deviceTypeId())
			.name(event.name())
			.description(event.description())
			.deviceTypeCreatedAt(event.createdAt())
			.deviceTypeUpdatedAt(event.updatedAt())
			.producedAt(event.producedAt())
			.build();
	}

	private DeviceTypeSnapshot toSnapshot(DeviceTypeEvent event) {
		return DeviceTypeSnapshot.builder()
			.deviceTypeId(event.deviceTypeId())
			.name(event.name())
			.description(event.description())
			.deviceTypeCreatedAt(event.createdAt())
			.deviceTypeUpdatedAt(event.updatedAt())
			.lastEventId(event.eventId())
			.lastEventType(event.eventType().name())
			.build();
	}
}
