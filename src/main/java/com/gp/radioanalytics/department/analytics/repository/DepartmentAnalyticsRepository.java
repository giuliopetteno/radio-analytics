package com.gp.radioanalytics.department.analytics.repository;

import com.gp.radioanalytics.department.analytics.dto.DepartmentSummary;
import com.gp.radioanalytics.department.snapshot.domain.DepartmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentAnalyticsRepository extends JpaRepository<DepartmentSnapshot, Long> {
	@Query("""
    SELECT new com.gp.radioanalytics.department.analytics.dto.DepartmentSummary(
        COUNT(dept),
        SUM(CASE WHEN EXISTS (
            SELECT 1 FROM DeviceSnapshot d WHERE d.departmentId = dept.departmentId AND d.deleted = false
        ) THEN 1 ELSE 0 END),
        SUM(CASE WHEN NOT EXISTS (
            SELECT 1 FROM DeviceSnapshot d WHERE d.departmentId = dept.departmentId AND d.deleted = false
        ) THEN 1 ELSE 0 END)
    )
    FROM DepartmentSnapshot dept
    WHERE dept.deleted = false
    """)
	DepartmentSummary getDepartmentSummary();
}
