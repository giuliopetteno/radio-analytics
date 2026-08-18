package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByOrganization;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;

import java.util.List;

public record KpiResults(
	KpiResult<DeviceSummary> deviceSummary,
	KpiResult<List<DevicesByOrganization>> devicesByOrganization,
	KpiResult<List<DevicesByDepartment>> devicesByDepartment,
	KpiResult<List<DevicesByType>> devicesByType,
	KpiResult<List<MonthlyCount>> devicesInstallationTrend,
	KpiResult<Double> averageDeviceAge,
	KpiResult<Summary> organizationSummary,
	KpiResult<Summary> departmentSummary,
	KpiResult<Summary> deviceTypeSummary,
	KpiResult<List<MonthlyCount>> devicesDecommissioningTrend,
	KpiResult<List<MonthlyEventsCount>> deviceEventsTrend,
	KpiResult<List<MonthlyEventsCount>> organizationEventsTrend,
	KpiResult<List<MonthlyEventsCount>> departmentEventsTrend,
	KpiResult<List<MonthlyEventsCount>> deviceTypeEventsTrend
) {}
