package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.device.analytics.dto.*;

import java.time.Instant;
import java.util.List;

public record AnalyticsResponse(
	AnalyticsTaskResult<DeviceSummary> deviceSummary,
	AnalyticsTaskResult<List<DevicesByOrganization>> devicesByOrganization,
	AnalyticsTaskResult<List<DevicesByDepartment>> devicesByDepartment,
	AnalyticsTaskResult<List<DevicesByType>> devicesByType,
	AnalyticsTaskResult<List<MonthlyCount>> devicesInstallationTrend,
	AnalyticsTaskResult<Double> averageDeviceAge,
	AnalyticsTaskResult<Summary> organizationSummary,
	AnalyticsTaskResult<Summary> departmentSummary,
	AnalyticsTaskResult<Summary> deviceTypeSummary,
	AnalyticsTaskResult<List<MonthlyCount>> devicesDecommissioningTrend,
	AnalyticsTaskResult<List<MonthlyEventsCount>> deviceEventsTrend,
	AnalyticsTaskResult<List<MonthlyEventsCount>> organizationEventsTrend,
	AnalyticsTaskResult<List<MonthlyEventsCount>> departmentEventsTrend,
	AnalyticsTaskResult<List<MonthlyEventsCount>> deviceTypeEventsTrend,
	AnalyticsStatus status,
	Instant generatedAt
) {}