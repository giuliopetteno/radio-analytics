package com.gp.radioanalytics.devicetype.analytics.service;

import com.gp.radioanalytics.devicetype.analytics.dto.DeviceTypeSummary;
import com.gp.radioanalytics.devicetype.analytics.repository.DeviceTypeAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceTypeAnalyticsService {
	private final DeviceTypeAnalyticsRepository deviceTypeAnalyticsRepository;

	public DeviceTypeSummary getDeviceTypeSummary() {
		return deviceTypeAnalyticsRepository.getDeviceTypeSummary();
	}
}
