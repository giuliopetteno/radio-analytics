package com.gp.radioanalytics.device.analytics.dto;

public record DeviceSummary(
	long total,
	long active,
	long pendingInstallation,
	long underMaintenance,
	long outOfService,
	long pendingDecommissioning,
	long decommissioned,
	long deleted
) {}