package com.gp.radioanalytics.analytics.report.analyticsreport.service;

import com.gp.radioanalytics.analytics.dto.Response;
import com.gp.radioanalytics.analytics.dto.KpiResults;
import com.gp.radioanalytics.analytics.enums.ExecutionStatus;
import com.gp.radioanalytics.analytics.report.analyticsreport.domain.AnalyticsReport;
import com.gp.radioanalytics.analytics.report.analyticsreport.repository.AnalyticsReportRepository;
import com.gp.radioanalytics.analytics.report.exception.ReportNotAvailableException;
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
	public void saveReport(KpiResults kpiResults, ExecutionStatus executionStatus, Instant generatedAt) {
			String json = jsonMapper.writeValueAsString(kpiResults);
			var report = AnalyticsReport.builder()
				.report(json)
				.reportExecutionStatus(executionStatus)
				.generatedAt(generatedAt.atOffset(ZoneOffset.UTC))
				.build();
			analyticsReportRepository.save(report);
	}

	public Response getLatestAnalyticsReport() {
		var report = analyticsReportRepository.findFirstByOrderByGeneratedAtDesc()
			.orElseThrow(() -> new ReportNotAvailableException("No analytics report available"));

		var kpiResults = jsonMapper.readValue(report.getReport(), KpiResults.class);

		return new Response(kpiResults, report.getReportExecutionStatus(), report.getGeneratedAt().toInstant());
	}
}
