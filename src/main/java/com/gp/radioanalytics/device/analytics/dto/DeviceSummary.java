package com.gp.radioanalytics.device.analytics.dto;

public record DeviceSummary(
	Long totalDevices,
	Long activeDevices,
	Long deletedDevices
) {}