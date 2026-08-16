package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByOrganization;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;

import java.util.List;

public record TaskResults(
	TaskResult<DeviceSummary> deviceSummary,
	TaskResult<List<DevicesByOrganization>> devicesByOrganization,
	TaskResult<List<DevicesByDepartment>> devicesByDepartment,
	TaskResult<List<DevicesByType>> devicesByType,
	TaskResult<List<MonthlyCount>> devicesInstallationTrend,
	TaskResult<Double> averageDeviceAge,
	TaskResult<Summary> organizationSummary,
	TaskResult<Summary> departmentSummary,
	TaskResult<Summary> deviceTypeSummary,
	TaskResult<List<MonthlyCount>> devicesDecommissioningTrend,
	TaskResult<List<MonthlyEventsCount>> deviceEventsTrend,
	TaskResult<List<MonthlyEventsCount>> organizationEventsTrend,
	TaskResult<List<MonthlyEventsCount>> departmentEventsTrend,
	TaskResult<List<MonthlyEventsCount>> deviceTypeEventsTrend
) {}
