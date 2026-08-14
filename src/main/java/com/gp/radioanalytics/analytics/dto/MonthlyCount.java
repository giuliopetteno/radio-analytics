package com.gp.radioanalytics.analytics.dto;

public record MonthlyCount(
	int year,
	int month,
	long total
) {}
