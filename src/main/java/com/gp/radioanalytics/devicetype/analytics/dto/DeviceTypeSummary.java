package com.gp.radioanalytics.devicetype.analytics.dto;

public record DeviceTypeSummary(
	long totalDeviceTypes,
	long assignedDeviceTypes,
	long unassignedDeviceTypes
) {}