package com.gp.radioanalytics.department.analytics.service;

import com.gp.radioanalytics.department.analytics.dto.DepartmentSummary;
import com.gp.radioanalytics.department.analytics.repository.DepartmentAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentAnalyticsService {
	private final DepartmentAnalyticsRepository departmentAnalyticsRepository;

	public DepartmentSummary getDepartmentSummary() {
		return departmentAnalyticsRepository.getDepartmentSummary();
	}
}
