package com.gp.radioanalytics.department.analytics.dto;

public record DepartmentSummary(
	long total,
	long withDevices,
	long withoutDevices
) {}