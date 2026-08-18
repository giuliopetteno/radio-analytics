package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByOrganization;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;

import java.util.List;

public record TimedSubTasks(
	TimedSubTask<DeviceSummary> deviceSummaryTask,
	TimedSubTask<List<DevicesByOrganization>> devicesByOrganizationTask,
	TimedSubTask<List<DevicesByDepartment>> devicesByDepartmentTask,
	TimedSubTask<List<DevicesByType>> devicesByTypeTask,
	TimedSubTask<List<MonthlyCount>> devicesInstallationTrendTask,
	TimedSubTask<Double> averageDeviceAgeTask,
	TimedSubTask<Summary> organizationSummaryTask,
	TimedSubTask<Summary> departmentSummaryTask,
	TimedSubTask<Summary> deviceTypeSummaryTask,
	TimedSubTask<List<MonthlyCount>> devicesDecommissioningTrendTask,
	TimedSubTask<List<MonthlyEventsCount>> deviceEventsTrendTask,
	TimedSubTask<List<MonthlyEventsCount>> organizationEventsTrendTask,
	TimedSubTask<List<MonthlyEventsCount>> departmentEventsTrendTask,
	TimedSubTask<List<MonthlyEventsCount>> deviceTypeEventsTrendTask
) {}
