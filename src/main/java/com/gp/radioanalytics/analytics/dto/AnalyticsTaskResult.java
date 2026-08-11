package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.AnalyticsTaskStatus;

public record AnalyticsTaskResult<T>(
	T data,
	AnalyticsTaskStatus status
) {}
