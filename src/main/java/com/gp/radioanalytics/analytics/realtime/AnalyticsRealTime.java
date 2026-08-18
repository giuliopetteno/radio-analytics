package com.gp.radioanalytics.analytics.realtime;

import com.gp.radioanalytics.analytics.dto.Response;
import com.gp.radioanalytics.analytics.engine.AnalyticsEngine;
import com.gp.radioanalytics.analytics.enums.ExecutionMode;
import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsRealTime {
	private final AnalyticsEngine analyticsEngine;

	@Value("${analytics.realtime.deadline.seconds}")
	private int deadlineInSeconds;

	public Response getRealTimeAnalytics() {
		try (var scope = StructuredTaskScope.open(Joiner.allUntil(_ -> false),
										config -> config.withTimeout(Duration.ofSeconds(deadlineInSeconds)))) {
			var executionResult = analyticsEngine.executeAnalytics(ExecutionMode.REALTIME, scope);

			return new Response(executionResult.kpiResults(), executionResult.executionStatus(), executionResult.generatedAt());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AnalyticsExecutionException("Analytics execution was interrupted", e);
		}
	}
}