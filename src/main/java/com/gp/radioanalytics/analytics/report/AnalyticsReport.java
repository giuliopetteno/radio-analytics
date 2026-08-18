package com.gp.radioanalytics.analytics.report;

import com.gp.radioanalytics.analytics.engine.AnalyticsEngine;
import com.gp.radioanalytics.analytics.enums.ExecutionMode;
import com.gp.radioanalytics.analytics.enums.ExecutionStatus;
import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import com.gp.radioanalytics.analytics.report.analyticsreport.service.AnalyticsReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.StructuredTaskScope;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsReport {
	private final AnalyticsEngine analyticsEngine;
	private final AnalyticsReportService analyticsReportService;

	@Value("${analytics.report.deadline.minutes}")
	private int deadlineInMinutes;

	public void generateAnalyticsReport() {
		try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allUntil(_ -> false),
			config -> config.withTimeout(Duration.ofMinutes(deadlineInMinutes)))) {
			var executionResult = analyticsEngine.executeAnalytics(ExecutionMode.REPORT, scope);

			if (executionResult.executionStatus() == ExecutionStatus.FAILED) {
				log.error("Report generation failed, some KPIs unavailable — skipping persistence");
				return;
			}

			analyticsReportService.saveReport(executionResult.kpiResults(), executionResult.executionStatus(), executionResult.generatedAt());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AnalyticsExecutionException("Analytics execution was interrupted", e);
		}
	}
}
