package com.gp.radioanalytics.device.analytics.dto;

public record DevicesByDepartment(
	Long departmentId,
	String departmentName,
	long total
) {}