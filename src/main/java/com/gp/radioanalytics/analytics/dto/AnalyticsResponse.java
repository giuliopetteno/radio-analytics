package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.department.analytics.dto.DepartmentSummary;
import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;
import com.gp.radioanalytics.devicetype.analytics.dto.DeviceTypeSummary;

import java.time.Instant;
import java.util.List;

public record AnalyticsResponse(
	AnalyticsTaskResult<DeviceSummary> deviceSummary,
	AnalyticsTaskResult<List<DevicesByType>> devicesByType,
	AnalyticsTaskResult<List<DevicesByDepartment>> devicesByDepartment,
	AnalyticsTaskResult<DeviceTypeSummary> deviceTypeSummary,
	AnalyticsTaskResult<DepartmentSummary> departmentSummary,
	AnalyticsStatus status,
	Instant generatedAt
) {}