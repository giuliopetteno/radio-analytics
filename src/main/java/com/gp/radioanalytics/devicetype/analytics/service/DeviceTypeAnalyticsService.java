package com.gp.radioanalytics.devicetype.analytics.service;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.Summary;
import com.gp.radioanalytics.devicetype.analytics.repository.DeviceTypeAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceTypeAnalyticsService {
	private final DeviceTypeAnalyticsRepository deviceTypeAnalyticsRepository;

	public Summary getDeviceTypeSummary() {
		return deviceTypeAnalyticsRepository.getDeviceTypeSummary();
	}

	public List<MonthlyEventsCount> getDeviceTypeEventsTrend() {
		return deviceTypeAnalyticsRepository.getDeviceTypeEventsTrend();
	}
}
