package com.gp.radioanalytics.device.analytics.dto;

public record DevicesByType(
	long deviceTypeId,
	String deviceTypeName,
	long total
) {}