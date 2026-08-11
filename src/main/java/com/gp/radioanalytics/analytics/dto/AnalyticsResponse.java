package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.device.analytics.dto.DeviceStatusCount;
import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;

import java.time.Instant;
import java.util.List;

public record AnalyticsResponse(
	AnalyticsTaskResult<DeviceSummary> deviceSummary,
	AnalyticsTaskResult<List<DeviceStatusCount>> devicesByStatus,
	AnalyticsStatus status,
	Instant generatedAt
) {}