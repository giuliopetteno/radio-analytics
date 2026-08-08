package com.gp.radioanalytics.organization.service;

import com.gp.radioanalytics.enums.EntityType;
import com.gp.radioanalytics.enums.EventType;
import com.gp.radioanalytics.kafka.event.OrganizationEvent;
import com.gp.radioanalytics.kafka.processedevent.service.ProcessedEventService;
import com.gp.radioanalytics.organization.domain.OrganizationEventLog;
import com.gp.radioanalytics.organization.domain.OrganizationSnapshot;
import com.gp.radioanalytics.organization.repository.OrganizationEventLogRepository;
import com.gp.radioanalytics.organization.repository.OrganizationSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationEventProcessingService {
	private final ProcessedEventService processedEventService;
	private final OrganizationEventLogRepository organizationEventLogRepository;
	private final OrganizationSnapshotRepository organizationSnapshotRepository;

	@Transactional
	public void process(OrganizationEvent event) {
		if (processedEventService.isProcessed(event.eventId())) {
			log.info("Event {} already processed, skipping", event.eventId());
			return;
		}

		organizationEventLogRepository.save(toEventLog(event));

		OrganizationSnapshot organizationSnapshot = toSnapshot(event);
		if (event.eventType() == EventType.DELETE) {
			organizationSnapshot.setDeleted(true);
			organizationSnapshot.setDeletedAt(event.producedAt());
		}
		organizationSnapshotRepository.save(organizationSnapshot);

		processedEventService.markAsProcessed(event.eventId(), EntityType.ORGANIZATION.name());
		log.info("Event {} processed successfully", event.eventId());
	}

	private OrganizationEventLog toEventLog(OrganizationEvent event) {
		return OrganizationEventLog.builder()
			.eventId(event.eventId())
			.eventType(event.eventType().name())
			.organizationId(event.organizationId())
			.name(event.name())
			.code(event.code())
			.description(event.description())
			.organizationCreatedAt(event.createdAt())
			.organizationUpdatedAt(event.updatedAt())
			.producedAt(event.producedAt())
			.build();
	}

	private OrganizationSnapshot toSnapshot(OrganizationEvent event) {
		return OrganizationSnapshot.builder()
			.organizationId(event.organizationId())
			.name(event.name())
			.code(event.code())
			.description(event.description())
			.organizationCreatedAt(event.createdAt())
			.organizationUpdatedAt(event.updatedAt())
			.lastEventId(event.eventId())
			.lastEventType(event.eventType().name())
			.build();
	}
}
