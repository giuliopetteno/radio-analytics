package com.gp.radioanalytics.analytics.realtime;

import com.gp.radioanalytics.analytics.dto.AnalyticsResponse;
import com.gp.radioanalytics.analytics.dto.AnalyticsTaskResult;
import com.gp.radioanalytics.analytics.enums.AnalyticsRequirement;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.analytics.enums.AnalyticsTaskStatus;
import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import com.gp.radioanalytics.device.analytics.service.DeviceAnalyticsService;
import com.gp.radioanalytics.devicetype.analytics.service.DeviceTypeAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsRealTimeOrchestrator {
	private final DeviceAnalyticsService deviceAnalyticsService;
	private final DeviceTypeAnalyticsService deviceTypeAnalyticsService;

	@Value("${analytics.realtime.deadline.seconds}")
	private int deadlineInSeconds;

	public AnalyticsResponse getRealTimeAnalytics() {
		try (var scope = StructuredTaskScope.open(Joiner.<Object>awaitAll(),
										config -> config.withTimeout(Duration.ofSeconds(deadlineInSeconds)))) {
			var deviceSummaryTask = scope.fork(deviceAnalyticsService::getDeviceSummary);
			var devicesByTypeTask = scope.fork(deviceAnalyticsService::getDevicesByType);
			var devicesByDepartmentTask = scope.fork(deviceAnalyticsService::getDevicesByDepartment);
			var deviceTypeSummaryTask = scope.fork(deviceTypeAnalyticsService::getDeviceTypeSummary);

			boolean deadlineExpired = false;
			try {
				scope.join();
			} catch (StructuredTaskScope.TimeoutException e) {
				deadlineExpired = true;
			}

			var deviceSummary = toResult(deviceSummaryTask, AnalyticsRequirement.MANDATORY, deadlineExpired);
			var devicesByType = toResult(devicesByTypeTask, AnalyticsRequirement.MANDATORY, deadlineExpired);
			var devicesByDepartment = toResult(devicesByDepartmentTask, AnalyticsRequirement.MANDATORY, deadlineExpired);
			var deviceTypeSummary = toResult(deviceTypeSummaryTask, AnalyticsRequirement.OPTIONAL, deadlineExpired);

			var taskList= List.of(
				deviceSummary,
				devicesByType,
				devicesByDepartment,
				deviceTypeSummary
			);

			var analyticsStatus = deriveAnalyticsStatus(taskList);

			return new AnalyticsResponse(deviceSummary, devicesByType, devicesByDepartment, deviceTypeSummary, analyticsStatus, Instant.now());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AnalyticsExecutionException("Analytics execution was interrupted", e);
		}
	}

	private <T> AnalyticsTaskResult<T> toResult(Subtask<T> task, AnalyticsRequirement requirement, boolean deadlineExpired) {
		return switch (task.state()) {
			case SUCCESS ->
				new AnalyticsTaskResult<>(
					task.get(),
					AnalyticsTaskStatus.SUCCESS,
					requirement
				);
			case FAILED ->
				new AnalyticsTaskResult<>(
					null,
					AnalyticsTaskStatus.FAILED,
					requirement
				);
			case UNAVAILABLE ->
				new AnalyticsTaskResult<>(
					null,
					deadlineExpired ? AnalyticsTaskStatus.TIMEOUT : AnalyticsTaskStatus.CANCELLED,
					requirement
				);
		};
	}

	private AnalyticsStatus deriveAnalyticsStatus(List<AnalyticsTaskResult<?>> results) {
		boolean mandatoryFailed = results.stream()
			.anyMatch(result ->
				result.requirement() == AnalyticsRequirement.MANDATORY
					&& result.status() != AnalyticsTaskStatus.SUCCESS
			);

		if (mandatoryFailed) {
			return AnalyticsStatus.FAILED;
		}

		boolean optionalFailed = results.stream()
			.anyMatch(result ->
				result.requirement() == AnalyticsRequirement.OPTIONAL
					&& result.status() != AnalyticsTaskStatus.SUCCESS
			);

		if (optionalFailed) {
			return AnalyticsStatus.PARTIAL;
		}

		return AnalyticsStatus.COMPLETED;
	}
}