package com.gp.radioanalytics.device.analytics.repository;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.MonthlyCount;
import com.gp.radioanalytics.device.analytics.dto.*;
import com.gp.radioanalytics.device.snapshot.domain.DeviceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceAnalyticsRepository extends JpaRepository<DeviceSnapshot, Long> {
	@Query("""
	SELECT new com.gp.radioanalytics.device.analytics.dto.DeviceSummary(
		SUM(CASE WHEN ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deviceStatus = 'ACTIVE' AND ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deviceStatus = 'PENDING_INSTALLATION' AND ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deviceStatus = 'UNDER_MAINTENANCE' AND ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deviceStatus = 'OUT_OF_SERVICE' AND ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deviceStatus = 'PENDING_DECOMMISSIONING' AND ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deviceStatus = 'DECOMMISSIONED' AND ds.deleted = false THEN 1 ELSE 0 END),
		SUM(CASE WHEN ds.deleted = true THEN 1 ELSE 0 END)
	)
	FROM DeviceSnapshot ds
	""")
	DeviceSummary getDeviceSummary();

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
        os.organization_id AS organizationId,
        os.name AS organizationName,
        COUNT(ds.device_id) AS total
    FROM radio_analytics.organization_snapshot os
    LEFT JOIN radio_analytics.device_snapshot ds
        ON ds.deleted = false
        AND (
            ds.organization_id = os.organization_id
            OR ds.department_id IN (
                SELECT dept_tree.department_id
                FROM department_tree dept_tree
                WHERE dept_tree.organization_id = os.organization_id
            )
        )
    WHERE os.deleted = false
    GROUP BY os.organization_id, os.name
    ORDER BY total DESC
    """, nativeQuery = true)
	List<DevicesByOrganization> getDevicesByOrganization();

	@Query("""
    SELECT new com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment(
        ds.departmentId,
        depts.name,
        COUNT(ds)
    )
    FROM DeviceSnapshot ds
    JOIN DepartmentSnapshot depts ON depts.departmentId = ds.departmentId
    WHERE ds.deleted = false AND ds.departmentId IS NOT NULL
    GROUP BY ds.departmentId, depts.name
    """)
	List<DevicesByDepartment> getDevicesByDepartment();

	@Query("""
    SELECT new com.gp.radioanalytics.device.analytics.dto.DevicesByType(
        ds.deviceTypeId,
        dts.name,
        COUNT(ds)
    )
    FROM DeviceSnapshot ds
    JOIN DeviceTypeSnapshot dts ON dts.deviceTypeId = ds.deviceTypeId
    WHERE ds.deleted = false
    GROUP BY ds.deviceTypeId, dts.name
    """)
	List<DevicesByType> getDevicesByType();

	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.MonthlyCount(
        YEAR(ds.installationDate),
        MONTH(ds.installationDate),
        COUNT(ds)
    )
    FROM DeviceSnapshot ds
    WHERE ds.deleted = false
    GROUP BY YEAR(ds.installationDate), MONTH(ds.installationDate)
    ORDER BY YEAR(ds.installationDate), MONTH(ds.installationDate)
    """)
	List<MonthlyCount> getDevicesInstallationTrend();

	@Query(value = """
    SELECT AVG(CURRENT_DATE - ds.installation_date)
    FROM radio_analytics.device_snapshot ds
    WHERE ds.deleted = false
    """, nativeQuery = true)
	Double getAverageDeviceAge();

	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.MonthlyCount(
        YEAR(ds.decommissionDate),
        MONTH(ds.decommissionDate),
        COUNT(ds)
    )
    FROM DeviceSnapshot ds
    WHERE ds.decommissionDate IS NOT NULL
    GROUP BY YEAR(ds.decommissionDate), MONTH(ds.decommissionDate)
    ORDER BY YEAR(ds.decommissionDate), MONTH(ds.decommissionDate)
    """)
	List<MonthlyCount> getDevicesDecommissioningTrend();

	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.MonthlyEventsCount(
        YEAR(del.producedAt),
        MONTH(del.producedAt),
        del.eventType,
        COUNT(del)
    )
    FROM DeviceEventLog del
    GROUP BY YEAR(del.producedAt), MONTH(del.producedAt), del.eventType
    ORDER BY YEAR(del.producedAt), MONTH(del.producedAt), del.eventType
    """)
	List<MonthlyEventsCount> getDeviceEventsTrend();
}