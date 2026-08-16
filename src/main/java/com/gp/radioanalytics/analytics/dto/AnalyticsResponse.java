package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;

import java.time.Instant;

public record AnalyticsResponse(
	TaskResults results,
	AnalyticsStatus status,
	Instant generatedAt
) {}