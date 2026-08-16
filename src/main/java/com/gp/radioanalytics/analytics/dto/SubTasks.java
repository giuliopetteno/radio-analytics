package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByOrganization;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;

import java.util.List;
import java.util.concurrent.StructuredTaskScope.Subtask;

public record SubTasks(
	Subtask<DeviceSummary> deviceSummaryTask,
	Subtask<List<DevicesByOrganization>> devicesByOrganizationTask,
	Subtask<List<DevicesByDepartment>> devicesByDepartmentTask,
	Subtask<List<DevicesByType>> devicesByTypeTask,
	Subtask<List<MonthlyCount>> devicesInstallationTrendTask,
	Subtask<Double> averageDeviceAgeTask,
	Subtask<Summary> organizationSummaryTask,
	Subtask<Summary> departmentSummaryTask,
	Subtask<Summary> deviceTypeSummaryTask,
	Subtask<List<MonthlyCount>> devicesDecommissioningTrendTask,
	Subtask<List<MonthlyEventsCount>> deviceEventsTrendTask,
	Subtask<List<MonthlyEventsCount>> organizationEventsTrendTask,
	Subtask<List<MonthlyEventsCount>> departmentEventsTrendTask,
	Subtask<List<MonthlyEventsCount>> deviceTypeEventsTrendTask
) {}
