package com.gp.radioanalytics.kafka.event;

import com.gp.radioanalytics.enums.EventType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrganizationEvent(
	UUID eventId,
	EventType eventType,
	Long organizationId,
	String name,
	String code,
	String description,
	OffsetDateTime createdAt,
	OffsetDateTime updatedAt,
	OffsetDateTime producedAt
) {}
