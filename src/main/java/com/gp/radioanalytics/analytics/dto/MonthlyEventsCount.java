package com.gp.radioanalytics.analytics.dto;

public record MonthlyEventsCount(
	int year,
	int month,
	String eventType,
	long total
) {}
