package com.gp.radioanalytics.devicetype.analytics.dto;

public record DeviceTypeSummary(
	long total,
	long withDevices,
	long withoutDevices
) {}