package com.gp.radioanalytics.kafka.event;

import com.gp.radioanalytics.enums.EventType;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DeviceEvent(
	UUID eventId,
	EventType eventType,
	Long deviceId,
	String name,
	Long deviceTypeId,
	String serialNumber,
	String description,
	LocalDate installationDate,
	String deviceStatus,
	LocalDate decommissionDate,
	Long organizationId,
	Long departmentId,
	OffsetDateTime createdAt,
	OffsetDateTime updatedAt,
	OffsetDateTime producedAt
) {}
