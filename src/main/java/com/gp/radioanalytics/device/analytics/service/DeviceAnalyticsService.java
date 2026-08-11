package com.gp.radioanalytics.device.analytics.service;

import com.gp.radioanalytics.device.analytics.dto.DeviceStatusCount;
import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.repository.DeviceAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceAnalyticsService {
	private final DeviceAnalyticsRepository deviceAnalyticsRepository;

	public DeviceSummary getDeviceSummary() {
		return deviceAnalyticsRepository.getSummary();
	}

	public List<DeviceStatusCount> countByStatus() {
		return deviceAnalyticsRepository.countByStatus();
	}
}
