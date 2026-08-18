package com.gp.radioanalytics.analytics.engine;

import com.gp.radioanalytics.analytics.dto.*;
import com.gp.radioanalytics.analytics.enums.AnalyticsExecutionMode;
import com.gp.radioanalytics.analytics.enums.AnalyticsRequirement;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.analytics.enums.AnalyticsTaskStatus;
import com.gp.radioanalytics.analytics.metrics.AnalyticsMetrics;
import com.gp.radioanalytics.department.analytics.service.DepartmentAnalyticsService;
import com.gp.radioanalytics.device.analytics.service.DeviceAnalyticsService;
import com.gp.radioanalytics.devicetype.analytics.service.DeviceTypeAnalyticsService;
import com.gp.radioanalytics.organization.analytics.service.OrganizationAnalyticsService;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicLong;

import static com.gp.radioanalytics.analytics.constant.AnalyticsConstants.KpiConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEngine {
	private final DeviceAnalyticsService deviceAnalyticsService;
	private final DeviceTypeAnalyticsService deviceTypeAnalyticsService;
	private final DepartmentAnalyticsService departmentAnalyticsService;
	private final OrganizationAnalyticsService organizationAnalyticsService;
	private final AnalyticsMetrics analyticsMetrics;
	private final Tracer analyticsTracer;

	public AnalyticsExecutionResult executeAnalytics(AnalyticsExecutionMode mode, StructuredTaskScope<Object, ?> scope) throws InterruptedException {
		log.info("Starting analytics execution: mode={}", mode);
		var startedAt = System.nanoTime();

		var timedSubTasks = forkAll(scope);
		scope.join();

		var taskResults = getTaskResults(mode, timedSubTasks);
		var analyticsStatus = getAnalyticsStatus(mode, taskResults);

		var duration = Duration.ofNanos(System.nanoTime() - startedAt);

		analyticsMetrics.recordAnalyticsExecution(mode, analyticsStatus, duration);

		log.info("Analytics execution ended: mode={}, status={}, duration={}ms", mode, analyticsStatus, duration.toMillis());
		return new AnalyticsExecutionResult(taskResults, analyticsStatus, Instant.now());
	}

	private TimedSubTasks forkAll(StructuredTaskScope<Object, ?> scope) {
		var deviceSummaryTask = forkTask(scope, deviceAnalyticsService::getDeviceSummary,
			DEVICE_SUMMARY_KPI);
		var devicesByOrganizationTask = forkTask(scope, deviceAnalyticsService::getDevicesByOrganization,
			DEVICES_BY_ORGANIZATION_KPI);
		var devicesByDepartmentTask = forkTask(scope, deviceAnalyticsService::getDevicesByDepartment,
			DEVICES_BY_DEPARTMENT_KPI);
		var devicesByTypeTask = forkTask(scope, deviceAnalyticsService::getDevicesByType,
			DEVICES_BY_TYPE_KPI);
		var devicesInstallationTrendTask = forkTask(scope, deviceAnalyticsService::getDevicesInstallationTrend,
			DEVICES_INSTALLATION_TREND_KPI);
		var averageDeviceAgeTask = forkTask(scope, deviceAnalyticsService::getAverageDeviceAge,
			AVERAGE_DEVICE_AGE_KPI);
		var organizationSummaryTask = forkTask(scope, organizationAnalyticsService::getOrganizationSummary,
			ORGANIZATION_SUMMARY_KPI);
		var departmentSummaryTask = forkTask(scope, departmentAnalyticsService::getDepartmentSummary,
			DEPARTMENT_SUMMARY_KPI);
		var deviceTypeSummaryTask = forkTask(scope, deviceTypeAnalyticsService::getDeviceTypeSummary,
			DEVICE_TYPE_SUMMARY_KPI);
		var devicesDecommissioningTrendTask = forkTask(scope, deviceAnalyticsService::getDevicesDecommissioningTrend,
			DEVICES_DECOMMISSIONING_TREND_KPI);
		var deviceEventsTrendTask = forkTask(scope, deviceAnalyticsService::getDeviceEventsTrend,
			DEVICE_EVENTS_TREND_KPI);
		var organizationEventsTrendTask = forkTask(scope, organizationAnalyticsService::getOrganizationEventsTrend,
			ORGANIZATION_EVENTS_TREND_KPI);
		var departmentEventsTrendTask = forkTask(scope, departmentAnalyticsService::getDepartmentEventsTrend,
			DEPARTMENT_EVENTS_TREND_KPI);
		var deviceTypeEventsTrendTask = forkTask(scope, deviceTypeAnalyticsService::getDeviceTypeEventsTrend,
			DEVICE_TYPE_EVENTS_TREND_KPI);

		return new TimedSubTasks(
			deviceSummaryTask, devicesByOrganizationTask, devicesByDepartmentTask, devicesByTypeTask, devicesInstallationTrendTask,
			averageDeviceAgeTask, organizationSummaryTask, departmentSummaryTask, deviceTypeSummaryTask, devicesDecommissioningTrendTask,
			deviceEventsTrendTask, organizationEventsTrendTask, departmentEventsTrendTask, deviceTypeEventsTrendTask
		);
	}

	private <T> TimedSubTask<T> forkTask(StructuredTaskScope<Object, ?> scope, Callable<T> supplier, String taskName) {
		var duration = new AtomicLong(-1);
		var parentContext = Context.current();

		var subTask = scope.fork(() -> {
			var startedAt = System.nanoTime();

			var span = analyticsTracer
				.spanBuilder("analytics.kpi." + taskName)
				.setParent(parentContext)
				.startSpan();

			try (var _ = span.makeCurrent()) {
				return supplier.call();
			} catch (Exception e) {
				span.recordException(e);
				span.setStatus(StatusCode.ERROR);
				throw e;
			} finally {
				duration.set(System.nanoTime() - startedAt);
				span.end();
			}
		});

		return new TimedSubTask<>(subTask, duration);
	}

	private TaskResults getTaskResults(AnalyticsExecutionMode mode, TimedSubTasks timedSubTasks) {
		var deviceSummary = toTaskResult(mode, timedSubTasks.deviceSummaryTask(), AnalyticsRequirement.MANDATORY,
			DEVICE_SUMMARY_KPI);
		var devicesByOrganization = toTaskResult(mode, timedSubTasks.devicesByOrganizationTask(), AnalyticsRequirement.MANDATORY,
			DEVICES_BY_ORGANIZATION_KPI);
		var devicesByDepartment = toTaskResult(mode, timedSubTasks.devicesByDepartmentTask(), AnalyticsRequirement.MANDATORY,
			DEVICES_BY_DEPARTMENT_KPI);
		var devicesByType = toTaskResult(mode, timedSubTasks.devicesByTypeTask(), AnalyticsRequirement.MANDATORY,
			DEVICES_BY_TYPE_KPI);
		var devicesInstallationTrend = toTaskResult(mode, timedSubTasks.devicesInstallationTrendTask(), AnalyticsRequirement.MANDATORY,
			DEVICES_INSTALLATION_TREND_KPI);
		var averageDeviceAge = toTaskResult(mode, timedSubTasks.averageDeviceAgeTask(), AnalyticsRequirement.MANDATORY,
			AVERAGE_DEVICE_AGE_KPI);
		var organizationSummary = toTaskResult(mode, timedSubTasks.organizationSummaryTask(), AnalyticsRequirement.OPTIONAL,
			ORGANIZATION_SUMMARY_KPI);
		var departmentSummary = toTaskResult(mode, timedSubTasks.departmentSummaryTask(), AnalyticsRequirement.OPTIONAL,
			DEPARTMENT_SUMMARY_KPI);
		var deviceTypeSummary = toTaskResult(mode, timedSubTasks.deviceTypeSummaryTask(), AnalyticsRequirement.OPTIONAL,
			DEVICE_TYPE_SUMMARY_KPI);
		var devicesDecommissioningTrend = toTaskResult(mode, timedSubTasks.devicesDecommissioningTrendTask(), AnalyticsRequirement.OPTIONAL,
			DEVICES_DECOMMISSIONING_TREND_KPI);
		var deviceEventsTrend = toTaskResult(mode, timedSubTasks.deviceEventsTrendTask(), AnalyticsRequirement.OPTIONAL,
			DEVICE_EVENTS_TREND_KPI);
		var organizationEventsTrend = toTaskResult(mode, timedSubTasks.organizationEventsTrendTask(), AnalyticsRequirement.OPTIONAL,
			ORGANIZATION_EVENTS_TREND_KPI);
		var departmentEventsTrend = toTaskResult(mode, timedSubTasks.departmentEventsTrendTask(), AnalyticsRequirement.OPTIONAL,
			DEPARTMENT_EVENTS_TREND_KPI);
		var deviceTypeEventsTrend = toTaskResult(mode, timedSubTasks.deviceTypeEventsTrendTask(), AnalyticsRequirement.OPTIONAL,
			DEVICE_TYPE_EVENTS_TREND_KPI);

		return new TaskResults(
			deviceSummary, devicesByOrganization, devicesByDepartment, devicesByType, devicesInstallationTrend, averageDeviceAge, organizationSummary,
			departmentSummary, deviceTypeSummary, devicesDecommissioningTrend, deviceEventsTrend, organizationEventsTrend, departmentEventsTrend,
			deviceTypeEventsTrend
		);
	}

	private <T> TaskResult<T> toTaskResult(AnalyticsExecutionMode mode, TimedSubTask<T> timedSubTask, AnalyticsRequirement requirement, String taskName) {
		var taskResult = switch (timedSubTask.subTask().state()) {
			case SUCCESS ->
				new TaskResult<>(
					timedSubTask.subTask().get(),
					AnalyticsTaskStatus.SUCCESS,
					requirement
				);
			case FAILED ->
				new TaskResult<T>(
					null,
					AnalyticsTaskStatus.FAILED,
					requirement
				);
			case UNAVAILABLE ->
				new TaskResult<T>(
					null,
					AnalyticsTaskStatus.TIMEOUT,
					requirement
				);
		};

		var duration = timedSubTask.duration().get();

		if (duration >= 0)
			analyticsMetrics.recordTaskExecution(mode, taskName, taskResult.status(), Duration.ofNanos(duration));

		return taskResult;
	}

	private AnalyticsStatus getAnalyticsStatus(AnalyticsExecutionMode executionMode, TaskResults taskResults) {
		var results = toList(taskResults);

		var mandatoryFailed = results.stream()
			.anyMatch(result ->
				result.requirement() == AnalyticsRequirement.MANDATORY
					&& result.status() != AnalyticsTaskStatus.SUCCESS
			);

		if (mandatoryFailed)
			return AnalyticsStatus.FAILED;

		var optionalFailed = results.stream()
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

	private List<TaskResult<?>> toList(TaskResults taskResults) {
		return List.of(
			taskResults.deviceSummary(), taskResults.devicesByOrganization(), taskResults.devicesByDepartment(), taskResults.devicesByType(),
			taskResults.devicesInstallationTrend(), taskResults.averageDeviceAge(), taskResults.organizationSummary(), taskResults.departmentSummary(),
			taskResults.deviceTypeSummary(), taskResults.devicesDecommissioningTrend(), taskResults.deviceEventsTrend(), taskResults.organizationEventsTrend(),
			taskResults.departmentEventsTrend(), taskResults.deviceTypeEventsTrend()
		);
	}
}
