package com.gp.radioanalytics.analytics.report;

import com.gp.radioanalytics.analytics.dto.SubTasks;
import com.gp.radioanalytics.analytics.engine.AnalyticsEngine;
import com.gp.radioanalytics.analytics.enums.AnalyticsExecutionMode;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import com.gp.radioanalytics.analytics.report.analyticsreport.service.AnalyticsReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsReportOrchestrator {
	private final AnalyticsEngine analyticsEngine;
	private final AnalyticsReportService analyticsReportService;

	@Value("${analytics.report.deadline.minutes}")
	private int deadlineInMinutes;

	public void generateAnalyticsReport() {
		try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.allUntil(_ -> false),
			config -> config.withTimeout(Duration.ofMinutes(deadlineInMinutes)))) {
			SubTasks subTasks = analyticsEngine.forkAll(scope);

			scope.join();

			var taskResults= analyticsEngine.getTaskResults(subTasks);
			var analyticsStatus = analyticsEngine.getAnalyticsStatus(taskResults, AnalyticsExecutionMode.REPORT);

			if (analyticsStatus == AnalyticsStatus.FAILED) {
				log.error("Report generation failed, some KPIs unavailable — skipping persistence");
				return;
			}
			analyticsReportService.saveReport(taskResults, analyticsStatus, Instant.now());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AnalyticsExecutionException("Analytics execution was interrupted", e);
		}
	}
}
