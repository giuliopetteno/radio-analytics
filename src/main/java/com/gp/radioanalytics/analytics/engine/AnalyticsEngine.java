package com.gp.radioanalytics.analytics.engine;

import com.gp.radioanalytics.analytics.dto.*;
import com.gp.radioanalytics.analytics.enums.ExecutionMode;
import com.gp.radioanalytics.analytics.enums.KpiRequirement;
import com.gp.radioanalytics.analytics.enums.ExecutionStatus;
import com.gp.radioanalytics.analytics.enums.KpiStatus;
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

	public ExecutionResult executeAnalytics(ExecutionMode mode, StructuredTaskScope<Object, ?> scope) throws InterruptedException {
		log.info("Starting analytics execution: mode={}", mode);
		var startedAt = System.nanoTime();

		var timedSubTasks = forkAll(scope);
		scope.join();

		var kpiResults = getKpiResults(mode, timedSubTasks);
		var executionStatus = getExecutionStatus(mode, kpiResults);

		var duration = Duration.ofNanos(System.nanoTime() - startedAt);

		analyticsMetrics.recordAnalyticsExecution(mode, executionStatus, duration);

		log.info("Analytics execution ended: mode={}, status={}, duration={}ms", mode, executionStatus, duration.toMillis());
		return new ExecutionResult(kpiResults, executionStatus, Instant.now());
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

	private KpiResults getKpiResults(ExecutionMode mode, TimedSubTasks timedSubTasks) {
		var deviceSummary = toKpiResult(mode, timedSubTasks.deviceSummaryTask(), KpiRequirement.MANDATORY,
			DEVICE_SUMMARY_KPI);
		var devicesByOrganization = toKpiResult(mode, timedSubTasks.devicesByOrganizationTask(), KpiRequirement.MANDATORY,
			DEVICES_BY_ORGANIZATION_KPI);
		var devicesByDepartment = toKpiResult(mode, timedSubTasks.devicesByDepartmentTask(), KpiRequirement.MANDATORY,
			DEVICES_BY_DEPARTMENT_KPI);
		var devicesByType = toKpiResult(mode, timedSubTasks.devicesByTypeTask(), KpiRequirement.MANDATORY,
			DEVICES_BY_TYPE_KPI);
		var devicesInstallationTrend = toKpiResult(mode, timedSubTasks.devicesInstallationTrendTask(), KpiRequirement.MANDATORY,
			DEVICES_INSTALLATION_TREND_KPI);
		var averageDeviceAge = toKpiResult(mode, timedSubTasks.averageDeviceAgeTask(), KpiRequirement.MANDATORY,
			AVERAGE_DEVICE_AGE_KPI);
		var organizationSummary = toKpiResult(mode, timedSubTasks.organizationSummaryTask(), KpiRequirement.OPTIONAL,
			ORGANIZATION_SUMMARY_KPI);
		var departmentSummary = toKpiResult(mode, timedSubTasks.departmentSummaryTask(), KpiRequirement.OPTIONAL,
			DEPARTMENT_SUMMARY_KPI);
		var deviceTypeSummary = toKpiResult(mode, timedSubTasks.deviceTypeSummaryTask(), KpiRequirement.OPTIONAL,
			DEVICE_TYPE_SUMMARY_KPI);
		var devicesDecommissioningTrend = toKpiResult(mode, timedSubTasks.devicesDecommissioningTrendTask(), KpiRequirement.OPTIONAL,
			DEVICES_DECOMMISSIONING_TREND_KPI);
		var deviceEventsTrend = toKpiResult(mode, timedSubTasks.deviceEventsTrendTask(), KpiRequirement.OPTIONAL,
			DEVICE_EVENTS_TREND_KPI);
		var organizationEventsTrend = toKpiResult(mode, timedSubTasks.organizationEventsTrendTask(), KpiRequirement.OPTIONAL,
			ORGANIZATION_EVENTS_TREND_KPI);
		var departmentEventsTrend = toKpiResult(mode, timedSubTasks.departmentEventsTrendTask(), KpiRequirement.OPTIONAL,
			DEPARTMENT_EVENTS_TREND_KPI);
		var deviceTypeEventsTrend = toKpiResult(mode, timedSubTasks.deviceTypeEventsTrendTask(), KpiRequirement.OPTIONAL,
			DEVICE_TYPE_EVENTS_TREND_KPI);

		return new KpiResults(
			deviceSummary, devicesByOrganization, devicesByDepartment, devicesByType, devicesInstallationTrend, averageDeviceAge, organizationSummary,
			departmentSummary, deviceTypeSummary, devicesDecommissioningTrend, deviceEventsTrend, organizationEventsTrend, departmentEventsTrend,
			deviceTypeEventsTrend
		);
	}

	private <T> KpiResult<T> toKpiResult(ExecutionMode mode, TimedSubTask<T> timedSubTask, KpiRequirement kpiRequirement, String kpiName) {
		var kpiResult = switch (timedSubTask.subTask().state()) {
			case SUCCESS ->
				new KpiResult<>(
					timedSubTask.subTask().get(),
					KpiStatus.SUCCESS,
					kpiRequirement
				);
			case FAILED ->
				new KpiResult<T>(
					null,
					KpiStatus.FAILED,
					kpiRequirement
				);
			case UNAVAILABLE ->
				new KpiResult<T>(
					null,
					KpiStatus.TIMEOUT,
					kpiRequirement
				);
		};

		var duration = timedSubTask.duration().get();

		if (duration >= 0)
			analyticsMetrics.recordKpiExecution(mode, kpiName, kpiResult.status(), Duration.ofNanos(duration));

		return kpiResult;
	}

	private ExecutionStatus getExecutionStatus(ExecutionMode executionMode, KpiResults kpiResults) {
		var kpiResultList = toKpiResultList(kpiResults);

		var mandatoryFailed = kpiResultList.stream()
			.anyMatch(kpiResult ->
				kpiResult.requirement() == KpiRequirement.MANDATORY
					&& kpiResult.status() != KpiStatus.SUCCESS
			);

		if (mandatoryFailed)
			return ExecutionStatus.FAILED;

		var optionalFailed = kpiResultList.stream()
			.anyMatch(kpiResult ->
				kpiResult.requirement() == KpiRequirement.OPTIONAL
					&& kpiResult.status() != KpiStatus.SUCCESS
			);

		if (optionalFailed) {
			if (executionMode == ExecutionMode.REALTIME)
				return ExecutionStatus.PARTIAL;
			else if (executionMode == ExecutionMode.REPORT)
				return ExecutionStatus.FAILED;
		}

		return ExecutionStatus.COMPLETED;
	}

	private List<KpiResult<?>> toKpiResultList(KpiResults kpiResults) {
		return List.of(
			kpiResults.deviceSummary(), kpiResults.devicesByOrganization(), kpiResults.devicesByDepartment(), kpiResults.devicesByType(),
			kpiResults.devicesInstallationTrend(), kpiResults.averageDeviceAge(), kpiResults.organizationSummary(), kpiResults.departmentSummary(),
			kpiResults.deviceTypeSummary(), kpiResults.devicesDecommissioningTrend(), kpiResults.deviceEventsTrend(), kpiResults.organizationEventsTrend(),
			kpiResults.departmentEventsTrend(), kpiResults.deviceTypeEventsTrend()
		);
	}
}
