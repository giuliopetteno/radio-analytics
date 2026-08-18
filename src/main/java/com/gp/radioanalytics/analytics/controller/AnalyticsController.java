package com.gp.radioanalytics.analytics.controller;

import com.gp.radioanalytics.analytics.dto.Response;
import com.gp.radioanalytics.analytics.realtime.AnalyticsRealTime;
import com.gp.radioanalytics.analytics.report.analyticsreport.service.AnalyticsReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gp.radioanalytics.constant.ApiConstants.ANALYTICS_PATH;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ANALYTICS_PATH)
@Tag(name = "Analytics controller", description = "API for realtime and scheduled analytics")
public class AnalyticsController {
	private final AnalyticsRealTime analyticsRealTime;
	private final AnalyticsReportService analyticsReportService;

	@GetMapping("/realtime")
	@Operation(summary = "Realtime analytics", description = "Returns realtime analytics.")
	public ResponseEntity<Response> getRealTimeAnalytics() {
		var response = analyticsRealTime.getRealTimeAnalytics();

		log.info("Realtime analytics generated with status {} at {}", response.executionStatus(), response.generatedAt());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/report/latest")
	@Operation(summary = "Latest analytics report", description = "Returns the most recent analytics report.")
	public ResponseEntity<Response> getLatestAnalyticsReport() {
		var response = analyticsReportService.getLatestAnalyticsReport();

		log.info("Latest analytics report returned with status {} at {}", response.executionStatus(), response.generatedAt());
		return ResponseEntity.ok(response);
	}
}
