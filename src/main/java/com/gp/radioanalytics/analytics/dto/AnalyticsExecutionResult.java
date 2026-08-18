package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;

import java.time.Instant;

public record AnalyticsExecutionResult(
	TaskResults taskResults,
	AnalyticsStatus analyticsStatus,
	Instant generatedAt
) {}
