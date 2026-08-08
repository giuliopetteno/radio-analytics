package com.gp.radioanalytics.kafka.event;

import com.gp.radioanalytics.enums.EventType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DepartmentEvent(
	UUID eventId,
	EventType eventType,
	Long departmentId,
	String name,
	String code,
	String description,
	Long organizationId,
	Long parentDepartmentId,
	OffsetDateTime createdAt,
	OffsetDateTime updatedAt,
	OffsetDateTime producedAt
) {}
