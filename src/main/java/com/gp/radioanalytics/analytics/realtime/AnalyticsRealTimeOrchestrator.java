package com.gp.radioanalytics.analytics.realtime;

import com.gp.radioanalytics.analytics.dto.AnalyticsResponse;
import com.gp.radioanalytics.analytics.dto.SubTasks;
import com.gp.radioanalytics.analytics.engine.AnalyticsEngine;
import com.gp.radioanalytics.analytics.enums.AnalyticsExecutionMode;
import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsRealTimeOrchestrator {
	private final AnalyticsEngine analyticsEngine;

	@Value("${analytics.realtime.deadline.seconds}")
	private int deadlineInSeconds;

	public AnalyticsResponse getRealTimeAnalytics() {
		try (var scope = StructuredTaskScope.open(Joiner.allUntil(_ -> false),
										config -> config.withTimeout(Duration.ofSeconds(deadlineInSeconds)))) {
			SubTasks subTasks = analyticsEngine.forkAll(scope);

			scope.join();

			var taskResults= analyticsEngine.getTaskResults(subTasks);
			var analyticsStatus = analyticsEngine.getAnalyticsStatus(taskResults, AnalyticsExecutionMode.REALTIME);

			return new AnalyticsResponse(taskResults, analyticsStatus, Instant.now());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AnalyticsExecutionException("Analytics execution was interrupted", e);
		}
	}
}