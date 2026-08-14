package com.gp.radioanalytics.device.analytics.dto;

public record DevicesByOrganization(
	Long organizationId,
	String organizationName,
	long total
) {}
