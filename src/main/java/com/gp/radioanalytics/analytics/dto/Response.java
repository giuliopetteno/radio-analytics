package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.ExecutionStatus;

import java.time.Instant;

public record Response(
	KpiResults kpiResults,
	ExecutionStatus executionStatus,
	Instant generatedAt
) {}