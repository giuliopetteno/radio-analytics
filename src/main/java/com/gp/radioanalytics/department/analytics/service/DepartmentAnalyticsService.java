package com.gp.radioanalytics.department.analytics.service;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.Summary;
import com.gp.radioanalytics.department.analytics.repository.DepartmentAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentAnalyticsService {
	private final DepartmentAnalyticsRepository departmentAnalyticsRepository;

	public Summary getDepartmentSummary() {
		return departmentAnalyticsRepository.getDepartmentSummary();
	}

	public List<MonthlyEventsCount> getDepartmentEventsTrend() {
		return departmentAnalyticsRepository.getDepartmentEventsTrend();
	}
}
