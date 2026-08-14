package com.gp.radioanalytics.analytics.dto;

public record Summary(
	long total,
	long withDevices,
	long withoutDevices,
	long deleted
) {}