package com.gp.radioanalytics.device.analytics.dto;

public record DeviceSummary(
	long totalDevices,
	long activeDevices,
	long pendingInstallationDevices,
	long maintenanceDevices,
	long outOfServiceDevices,
	long pendingDecommissioningDevices,
	long decommissionedDevices,
	long deletedDevices
) {}