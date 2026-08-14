package com.gp.radioanalytics.organization.analytics.service;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.Summary;
import com.gp.radioanalytics.organization.analytics.repository.OrganizationAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationAnalyticsService {
	private final OrganizationAnalyticsRepository organizationAnalyticsRepository;

	public Summary getOrganizationSummary() {
		return organizationAnalyticsRepository.getOrganizationSummary();
	}

	public List<MonthlyEventsCount> getOrganizationEventsTrend() {
		return organizationAnalyticsRepository.getOrganizationEventsTrend();
	}
}
