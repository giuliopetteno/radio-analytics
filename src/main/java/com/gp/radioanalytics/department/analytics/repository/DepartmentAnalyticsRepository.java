package com.gp.radioanalytics.department.analytics.repository;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.Summary;
import com.gp.radioanalytics.department.snapshot.domain.DepartmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentAnalyticsRepository extends JpaRepository<DepartmentSnapshot, Long> {
	@Query("""
	SELECT new com.gp.radioanalytics.analytics.dto.Summary(
		SUM(CASE WHEN depts.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN depts.deleted = false AND EXISTS (
			SELECT 1 FROM DeviceSnapshot ds WHERE ds.departmentId = depts.departmentId AND ds.deleted = false
		) THEN 1 ELSE 0 END),
		SUM(CASE WHEN depts.deleted = false AND NOT EXISTS (
			SELECT 1 FROM DeviceSnapshot ds WHERE ds.departmentId = depts.departmentId AND ds.deleted = false
		) THEN 1 ELSE 0 END),
		SUM(CASE WHEN depts.deleted = true THEN 1 ELSE 0 END)
	)
	FROM DepartmentSnapshot depts
    """)
	Summary getDepartmentSummary();

	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.MonthlyEventsCount(
        YEAR(deptel.producedAt),
        MONTH(deptel.producedAt),
        deptel.eventType,
        COUNT(deptel)
    )
    FROM DepartmentEventLog deptel
    GROUP BY YEAR(deptel.producedAt), MONTH(deptel.producedAt), deptel.eventType
    ORDER BY YEAR(deptel.producedAt), MONTH(deptel.producedAt), deptel.eventType
    """)
	List<MonthlyEventsCount> getDepartmentEventsTrend();
}
