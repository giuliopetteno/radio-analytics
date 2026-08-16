package com.gp.radioanalytics.analytics.report.analyticsreport.service;

import com.gp.radioanalytics.analytics.dto.AnalyticsResponse;
import com.gp.radioanalytics.analytics.dto.TaskResults;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.analytics.exception.AnalyticsExecutionException;
import com.gp.radioanalytics.analytics.report.analyticsreport.domain.AnalyticsReport;
import com.gp.radioanalytics.analytics.report.analyticsreport.repository.AnalyticsReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class AnalyticsReportService {
	private final JsonMapper jsonMapper;
	private final AnalyticsReportRepository analyticsReportRepository;

	@Transactional
	public void saveReport(TaskResults results, AnalyticsStatus status, Instant generatedAt) {
			String json = jsonMapper.writeValueAsString(results);
			var report = AnalyticsReport.builder()
				.report(json)
				.reportStatus(status)
				.generatedAt(generatedAt.atOffset(ZoneOffset.UTC))
				.build();
			analyticsReportRepository.save(report);
	}

	public AnalyticsResponse getLatestAnalyticsReport() {
		var report = analyticsReportRepository.findFirstByOrderByGeneratedAtDesc()
			.orElseThrow(() -> new AnalyticsExecutionException("No analytics report available yet"));

		var taskResults = jsonMapper.readValue(report.getReport(), TaskResults.class);

		return new AnalyticsResponse(taskResults, report.getReportStatus(), report.getGeneratedAt().toInstant());
	}
}
