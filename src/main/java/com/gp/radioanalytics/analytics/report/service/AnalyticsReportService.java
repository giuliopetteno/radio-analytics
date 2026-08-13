package com.gp.radioanalytics.analytics.report.service;

import com.gp.radioanalytics.analytics.dto.AnalyticsResponse;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AnalyticsReportService {
	public AnalyticsResponse getLatestAnalyticsReport() {
		return new AnalyticsResponse(null, null, null, null, AnalyticsStatus.COMPLETED, Instant.now());
	}
}
