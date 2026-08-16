package com.gp.radioanalytics.analytics.report.analyticsreport.repository;

import com.gp.radioanalytics.analytics.report.analyticsreport.domain.AnalyticsReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalyticsReportRepository extends JpaRepository<AnalyticsReport, Long> {
	Optional<AnalyticsReport> findFirstByOrderByGeneratedAtDesc();
}
