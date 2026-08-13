package com.gp.radioanalytics.device.analytics.service;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;
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

	public List<DevicesByType> getDevicesByType() {
		return deviceAnalyticsRepository.getDevicesByType();
	}

	public List<DevicesByDepartment> getDevicesByDepartment() {
		return deviceAnalyticsRepository.getDevicesByDepartment();
	}
}