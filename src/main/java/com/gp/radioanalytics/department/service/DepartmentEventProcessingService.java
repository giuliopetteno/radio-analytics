package com.gp.radioanalytics.department.service;

import com.gp.radioanalytics.department.eventlog.domain.DepartmentEventLog;
import com.gp.radioanalytics.department.snapshot.domain.DepartmentSnapshot;
import com.gp.radioanalytics.department.eventlog.repository.DepartmentEventLogRepository;
import com.gp.radioanalytics.department.snapshot.repository.DepartmentSnapshotRepository;
import com.gp.radioanalytics.enums.EntityType;
import com.gp.radioanalytics.enums.EventType;
import com.gp.radioanalytics.kafka.event.DepartmentEvent;
import com.gp.radioanalytics.kafka.processedevent.service.ProcessedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentEventProcessingService {
	private final ProcessedEventService processedEventService;
	private final DepartmentEventLogRepository departmentEventLogRepository;
	private final DepartmentSnapshotRepository departmentSnapshotRepository;

	@Transactional
	public void process(DepartmentEvent event) {
		if (processedEventService.isProcessed(event.eventId())) {
			log.info("Event {} already processed, skipping", event.eventId());
			return;
		}

		departmentEventLogRepository.save(toEventLog(event));

		var isNewerThanSnapshot = departmentSnapshotRepository.findById(event.departmentId())
			.map(snapshot -> event.producedAt().isAfter(snapshot.getLastEventProducedAt()))
			.orElse(true);

		if(isNewerThanSnapshot) {
			DepartmentSnapshot departmentSnapshot = toSnapshot(event);
			if (event.eventType() == EventType.DELETE) {
				departmentSnapshot.setDeleted(true);
				departmentSnapshot.setDeletedAt(event.producedAt());
			}
			departmentSnapshotRepository.save(departmentSnapshot);
		}

		processedEventService.markAsProcessed(event.eventId(), EntityType.DEPARTMENT.name());
		log.info("Event {} processed successfully", event.eventId());
	}

	private DepartmentEventLog toEventLog(DepartmentEvent event) {
		return DepartmentEventLog.builder()
			.eventId(event.eventId())
			.eventType(event.eventType().name())
			.departmentId(event.departmentId())
			.name(event.name())
			.code(event.code())
			.description(event.description())
			.organizationId(event.organizationId())
			.parentDepartmentId(event.parentDepartmentId())
			.departmentCreatedAt(event.createdAt())
			.departmentUpdatedAt(event.updatedAt())
			.producedAt(event.producedAt())
			.build();
	}

	private DepartmentSnapshot toSnapshot(DepartmentEvent event) {
		return DepartmentSnapshot.builder()
			.departmentId(event.departmentId())
			.name(event.name())
			.code(event.code())
			.description(event.description())
			.organizationId(event.organizationId())
			.parentDepartmentId(event.parentDepartmentId())
			.departmentCreatedAt(event.createdAt())
			.departmentUpdatedAt(event.updatedAt())
			.lastEventId(event.eventId())
			.lastEventType(event.eventType().name())
			.lastEventProducedAt(event.producedAt())
			.build();
	}
}
