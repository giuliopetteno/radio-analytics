package com.gp.radioanalytics.analytics.engine;

import com.gp.radioanalytics.analytics.dto.SubTasks;
import com.gp.radioanalytics.analytics.dto.TaskResult;
import com.gp.radioanalytics.analytics.dto.TaskResults;
import com.gp.radioanalytics.analytics.enums.AnalyticsExecutionMode;
import com.gp.radioanalytics.analytics.enums.AnalyticsRequirement;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.analytics.enums.AnalyticsTaskStatus;
import com.gp.radioanalytics.department.analytics.service.DepartmentAnalyticsService;
import com.gp.radioanalytics.device.analytics.service.DeviceAnalyticsService;
import com.gp.radioanalytics.devicetype.analytics.service.DeviceTypeAnalyticsService;
import com.gp.radioanalytics.organization.analytics.service.OrganizationAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEngine {
	private final DeviceAnalyticsService deviceAnalyticsService;
	private final DeviceTypeAnalyticsService deviceTypeAnalyticsService;
	private final DepartmentAnalyticsService departmentAnalyticsService;
	private final OrganizationAnalyticsService organizationAnalyticsService;

	public SubTasks forkAll(StructuredTaskScope<Object, ?> scope){
		var deviceSummaryTask = scope.fork(deviceAnalyticsService::getDeviceSummary);
		var devicesByOrganizationTask = scope.fork(deviceAnalyticsService::getDevicesByOrganization);
		var devicesByDepartmentTask = scope.fork(deviceAnalyticsService::getDevicesByDepartment);
		var devicesByTypeTask = scope.fork(deviceAnalyticsService::getDevicesByType);
		var devicesInstallationTrendTask = scope.fork(deviceAnalyticsService::getDevicesInstallationTrend);
		var averageDeviceAgeTask = scope.fork(deviceAnalyticsService::getAverageDeviceAge);
		var organizationSummaryTask = scope.fork(organizationAnalyticsService::getOrganizationSummary);
		var departmentSummaryTask = scope.fork(departmentAnalyticsService::getDepartmentSummary);
		var deviceTypeSummaryTask = scope.fork(deviceTypeAnalyticsService::getDeviceTypeSummary);
		var devicesDecommissioningTrendTask = scope.fork(deviceAnalyticsService::getDevicesDecommissioningTrend);
		var deviceEventsTrendTask = scope.fork(deviceAnalyticsService::getDeviceEventsTrend);
		var organizationEventsTrendTask = scope.fork(organizationAnalyticsService::getOrganizationEventsTrend);
		var departmentEventsTrendTask = scope.fork(departmentAnalyticsService::getDepartmentEventsTrend);
		var deviceTypeEventsTrendTask = scope.fork(deviceTypeAnalyticsService::getDeviceTypeEventsTrend);

		return new SubTasks(
			deviceSummaryTask, devicesByOrganizationTask, devicesByDepartmentTask, devicesByTypeTask, devicesInstallationTrendTask,
			averageDeviceAgeTask, organizationSummaryTask, departmentSummaryTask, deviceTypeSummaryTask, devicesDecommissioningTrendTask,
			deviceEventsTrendTask, organizationEventsTrendTask, departmentEventsTrendTask, deviceTypeEventsTrendTask
		);
	}

	public TaskResults getTaskResults(SubTasks subTasks) {
		var deviceSummary = toTaskResult(subTasks.deviceSummaryTask(), AnalyticsRequirement.MANDATORY);
		var devicesByOrganization = toTaskResult(subTasks.devicesByOrganizationTask(), AnalyticsRequirement.MANDATORY);
		var devicesByDepartment = toTaskResult(subTasks.devicesByDepartmentTask(), AnalyticsRequirement.MANDATORY);
		var devicesByType = toTaskResult(subTasks.devicesByTypeTask(), AnalyticsRequirement.MANDATORY);
		var devicesInstallationTrend = toTaskResult(subTasks.devicesInstallationTrendTask(), AnalyticsRequirement.MANDATORY);
		var averageDeviceAge = toTaskResult(subTasks.averageDeviceAgeTask(), AnalyticsRequirement.MANDATORY);
		var organizationSummary = toTaskResult(subTasks.organizationSummaryTask(), AnalyticsRequirement.OPTIONAL);
		var departmentSummary = toTaskResult(subTasks.departmentSummaryTask(), AnalyticsRequirement.OPTIONAL);
		var deviceTypeSummary = toTaskResult(subTasks.deviceTypeSummaryTask(), AnalyticsRequirement.OPTIONAL);
		var devicesDecommissioningTrend = toTaskResult(subTasks.devicesDecommissioningTrendTask(), AnalyticsRequirement.OPTIONAL);
		var deviceEventsTrend = toTaskResult(subTasks.deviceEventsTrendTask(), AnalyticsRequirement.OPTIONAL);
		var organizationEventsTrend = toTaskResult(subTasks.organizationEventsTrendTask(), AnalyticsRequirement.OPTIONAL);
		var departmentEventsTrend = toTaskResult(subTasks.departmentEventsTrendTask(), AnalyticsRequirement.OPTIONAL);
		var deviceTypeEventsTrend = toTaskResult(subTasks.deviceTypeEventsTrendTask(), AnalyticsRequirement.OPTIONAL);

		return new TaskResults(
			deviceSummary, devicesByOrganization, devicesByDepartment, devicesByType, devicesInstallationTrend, averageDeviceAge, organizationSummary,
			departmentSummary, deviceTypeSummary, devicesDecommissioningTrend, deviceEventsTrend, organizationEventsTrend, departmentEventsTrend,
			deviceTypeEventsTrend
		);
	}

	public <T> TaskResult<T> toTaskResult(StructuredTaskScope.Subtask<T> task, AnalyticsRequirement requirement) {
		return switch (task.state()) {
			case SUCCESS ->
				new TaskResult<>(
					task.get(),
					AnalyticsTaskStatus.SUCCESS,
					requirement
				);
			case FAILED ->
				new TaskResult<>(
					null,
					AnalyticsTaskStatus.FAILED,
					requirement
				);
			case UNAVAILABLE ->
				new TaskResult<>(
					null,
					AnalyticsTaskStatus.TIMEOUT,
					requirement
				);
		};
	}

	public AnalyticsStatus getAnalyticsStatus(TaskResults taskResults, AnalyticsExecutionMode executionMode) {
		var results = toList(taskResults);

		boolean mandatoryFailed = results.stream()
			.anyMatch(result ->
				result.requirement() == AnalyticsRequirement.MANDATORY
					&& result.status() != AnalyticsTaskStatus.SUCCESS
			);

		if (mandatoryFailed)
			return AnalyticsStatus.FAILED;

		boolean optionalFailed = results.stream()
			.anyMatch(result ->
				result.requirement() == AnalyticsRequirement.OPTIONAL
					&& result.status() != AnalyticsTaskStatus.SUCCESS
			);

		if (optionalFailed) {
			if (executionMode == AnalyticsExecutionMode.REALTIME)
				return AnalyticsStatus.PARTIAL;
			else if (executionMode == AnalyticsExecutionMode.REPORT)
				return AnalyticsStatus.FAILED;
		}

		return AnalyticsStatus.COMPLETED;
	}

	public List<TaskResult<?>> toList(TaskResults taskResults) {
		return List.of(
			taskResults.deviceSummary(), taskResults.devicesByOrganization(), taskResults.devicesByDepartment(), taskResults.devicesByType(),
			taskResults.devicesInstallationTrend(), taskResults.averageDeviceAge(), taskResults.organizationSummary(), taskResults.departmentSummary(),
			taskResults.deviceTypeSummary(), taskResults.devicesDecommissioningTrend(), taskResults.deviceEventsTrend(), taskResults.organizationEventsTrend(),
			taskResults.departmentEventsTrend(), taskResults.deviceTypeEventsTrend()
		);
	}
}
