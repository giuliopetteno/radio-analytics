package com.gp.radioanalytics.analytics.report.scheduler;

import com.gp.radioanalytics.analytics.report.AnalyticsReportOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsReportScheduler {
	private final AnalyticsReportOrchestrator analyticsReportOrchestrator;

	@Scheduled(cron = "${analytics.report.job.cron}")
	public void analyticsReportJob() {
		log.info("Starting analytics report job");

		analyticsReportOrchestrator.generateAnalyticsReport();

		log.info("Analytics report job finished");
	}
}
