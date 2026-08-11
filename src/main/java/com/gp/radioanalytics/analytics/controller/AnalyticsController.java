package com.gp.radioanalytics.analytics.controller;

import com.gp.radioanalytics.analytics.dto.AnalyticsResponse;
import com.gp.radioanalytics.analytics.realtime.AnalyticsRealTimeOrchestrator;
import com.gp.radioanalytics.analytics.report.service.AnalyticsReportService;
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
@Tag(name = "Analytics controller", description = "API for real-time and precomputed analytics")
public class AnalyticsController {
	private final AnalyticsRealTimeOrchestrator analyticsRealTimeOrchestrator;
	private final AnalyticsReportService analyticsReportService;

	@GetMapping("/realtime")
	@Operation(summary = "Real-time analytics", description = "Returns real-time analytics.")
	public ResponseEntity<AnalyticsResponse> getRealTimeAnalytics() {
		var analyticsResponse = analyticsRealTimeOrchestrator.computeAnalytics();

		log.info("Real-time analytics computed with status {}", analyticsResponse.status());
		return ResponseEntity.ok(analyticsResponse);
	}

	@GetMapping("/report/latest")
	@Operation(summary = "Latest analytics report", description = "Returns the most recent analytics report.")
	public ResponseEntity<AnalyticsResponse> getLatestReport() {
		var analyticsResponse = analyticsReportService.getLatestReport();

		log.info("Latest analytics report returned, generated at {}", analyticsResponse.generatedAt());
		return ResponseEntity.ok(analyticsResponse);
	}
}
