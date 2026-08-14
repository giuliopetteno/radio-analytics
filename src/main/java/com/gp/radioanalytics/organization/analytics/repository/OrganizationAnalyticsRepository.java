package com.gp.radioanalytics.organization.analytics.repository;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.Summary;
import com.gp.radioanalytics.organization.snapshot.domain.OrganizationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationAnalyticsRepository extends JpaRepository<OrganizationSnapshot, Long> {
	@Query(value = """
    WITH RECURSIVE department_tree AS (
        SELECT department_id, organization_id
        FROM radio_analytics.department_snapshot
        WHERE deleted = false
          AND organization_id IS NOT NULL

        UNION ALL

        SELECT child.department_id, parent.organization_id
        FROM radio_analytics.department_snapshot child
        JOIN department_tree parent
          ON child.parent_department_id = parent.department_id
        WHERE child.deleted = false
    )
    SELECT
        COUNT(*) FILTER (WHERE os.deleted = false) AS total,
        COUNT(*) FILTER (
            WHERE os.deleted = false
              AND EXISTS (
                  SELECT 1
                  FROM radio_analytics.device_snapshot ds
                  WHERE ds.deleted = false
                    AND (
                        ds.organization_id = os.organization_id
                        OR ds.department_id IN (
                            SELECT dept_tree.department_id
                            FROM department_tree dept_tree
                            WHERE dept_tree.organization_id = os.organization_id
                        )
                    )
              )
        ) AS withDevices,
        COUNT(*) FILTER (
            WHERE os.deleted = false
              AND NOT EXISTS (
                  SELECT 1
                  FROM radio_analytics.device_snapshot ds
                  WHERE ds.deleted = false
                    AND (
                        ds.organization_id = os.organization_id
                        OR ds.department_id IN (
                            SELECT dept_tree.department_id
                            FROM department_tree dept_tree
                            WHERE dept_tree.organization_id = os.organization_id
                        )
                    )
              )
        ) AS withoutDevices,
        COUNT(*) FILTER (WHERE os.deleted = true) AS deleted
    FROM radio_analytics.organization_snapshot os
    """, nativeQuery = true)
	Summary getOrganizationSummary();

	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.MonthlyEventsCount(
        YEAR(otel.producedAt),
        MONTH(otel.producedAt),
        otel.eventType,
        COUNT(otel)
    )
    FROM OrganizationEventLog otel
    GROUP BY YEAR(otel.producedAt), MONTH(otel.producedAt), otel.eventType
    ORDER BY YEAR(otel.producedAt), MONTH(otel.producedAt), otel.eventType
    """)
	List<MonthlyEventsCount> getOrganizationEventsTrend();
}
