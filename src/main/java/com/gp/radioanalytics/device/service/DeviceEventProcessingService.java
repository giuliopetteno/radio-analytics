package com.gp.radioanalytics.device.service;

import com.gp.radioanalytics.device.domain.DeviceEventLog;
import com.gp.radioanalytics.device.domain.DeviceSnapshot;
import com.gp.radioanalytics.device.repository.DeviceEventLogRepository;
import com.gp.radioanalytics.device.repository.DeviceSnapshotRepository;
import com.gp.radioanalytics.enums.EntityType;
import com.gp.radioanalytics.enums.EventType;
import com.gp.radioanalytics.kafka.event.DeviceEvent;
import com.gp.radioanalytics.kafka.processedevent.service.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceEventProcessingService {
	private final ProcessedEventService processedEventService;
	private final DeviceEventLogRepository deviceEventLogRepository;
	private final DeviceSnapshotRepository deviceSnapshotRepository;

	@Transactional
	public void process(DeviceEvent event) {
		if (processedEventService.isProcessed(event.eventId())) {
			log.info("Event {} already processed, skipping", event.eventId());
			return;
		}

		deviceEventLogRepository.save(toEventLog(event));

		DeviceSnapshot deviceSnapshot = toSnapshot(event);
		if (event.eventType() == EventType.DELETE) {
			deviceSnapshot.setDeleted(true);
			deviceSnapshot.setDeletedAt(event.producedAt());
		}
		deviceSnapshotRepository.save(deviceSnapshot);

		processedEventService.markAsProcessed(event.eventId(), EntityType.DEVICE.name());
		log.info("Event {} processed successfully", event.eventId());
	}

	private DeviceEventLog toEventLog(DeviceEvent event) {
		return DeviceEventLog.builder()
			.eventId(event.eventId())
			.eventType(event.eventType().name())
			.deviceId(event.deviceId())
			.name(event.name())
			.deviceTypeId(event.deviceTypeId())
			.serialNumber(event.serialNumber())
			.description(event.description())
			.installationDate(event.installationDate())
			.deviceStatus(event.deviceStatus())
			.decommissionDate(event.decommissionDate())
			.organizationId(event.organizationId())
			.departmentId(event.departmentId())
			.deviceCreatedAt(event.createdAt())
			.deviceUpdatedAt(event.updatedAt())
			.producedAt(event.producedAt())
			.build();
	}

	private DeviceSnapshot toSnapshot(DeviceEvent event) {
		return DeviceSnapshot.builder()
			.deviceId(event.deviceId())
			.name(event.name())
			.deviceTypeId(event.deviceTypeId())
			.serialNumber(event.serialNumber())
			.deviceStatus(event.deviceStatus())
			.decommissionDate(event.decommissionDate())
			.organizationId(event.organizationId())
			.departmentId(event.departmentId())
			.deviceCreatedAt(event.createdAt())
			.deviceUpdatedAt(event.updatedAt())
			.lastEventId(event.eventId())
			.lastEventType(event.eventType().name())
			.build();
	}
}
