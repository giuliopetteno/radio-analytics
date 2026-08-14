package com.gp.radioanalytics.device.analytics.service;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.MonthlyCount;
import com.gp.radioanalytics.device.analytics.dto.*;
import com.gp.radioanalytics.device.analytics.repository.DeviceAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceAnalyticsService {
	private final DeviceAnalyticsRepository deviceAnalyticsRepository;

	public DeviceSummary getDeviceSummary() {
		return deviceAnalyticsRepository.getDeviceSummary();
	}

	public List<DevicesByOrganization> getDevicesByOrganization() {
		return deviceAnalyticsRepository.getDevicesByOrganization();
	}

	public List<DevicesByDepartment> getDevicesByDepartment() {
		return deviceAnalyticsRepository.getDevicesByDepartment();
	}

	public List<DevicesByType> getDevicesByType() {
		return deviceAnalyticsRepository.getDevicesByType();
	}

	public List<MonthlyCount> getDevicesInstallationTrend() {
		return deviceAnalyticsRepository.getDevicesInstallationTrend();
	}

	public Double getAverageDeviceAge() {
		return deviceAnalyticsRepository.getAverageDeviceAge();
	}

	public List<MonthlyCount> getDevicesDecommissioningTrend() {
		return deviceAnalyticsRepository.getDevicesDecommissioningTrend();
	}

	public List<MonthlyEventsCount> getDeviceEventsTrend() {
		return deviceAnalyticsRepository.getDeviceEventsTrend();
	}
}