package com.gp.radioanalytics.analytics.report.scheduler;

import com.gp.radioanalytics.analytics.report.AnalyticsReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsReportScheduler {
	private final AnalyticsReport analyticsReport;

	@Scheduled(cron = "${analytics.report.job.cron}")
	public void analyticsReportJob() {
		log.info("Analytics report job started");
		analyticsReport.generateAnalyticsReport();
	}
}
