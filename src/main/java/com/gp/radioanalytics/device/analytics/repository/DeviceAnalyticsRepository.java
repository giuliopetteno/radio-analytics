package com.gp.radioanalytics.device.analytics.repository;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment;
import com.gp.radioanalytics.device.analytics.dto.DevicesByType;
import com.gp.radioanalytics.device.snapshot.domain.DeviceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceAnalyticsRepository extends JpaRepository<DeviceSnapshot, Long> {
	@Query("""
        SELECT new com.gp.radioanalytics.device.analytics.dto.DeviceSummary(
            SUM(CASE WHEN d.deleted = false THEN 1 ELSE 0 END),
			SUM(CASE WHEN d.deviceStatus = 'ACTIVE' AND d.deleted = false THEN 1 ELSE 0 END),
			SUM(CASE WHEN d.deviceStatus = 'PENDING_INSTALLATION' AND d.deleted = false THEN 1 ELSE 0 END),
			SUM(CASE WHEN d.deviceStatus = 'MAINTENANCE' AND d.deleted = false THEN 1 ELSE 0 END),
			SUM(CASE WHEN d.deviceStatus = 'OUT_OF_SERVICE' AND d.deleted = false THEN 1 ELSE 0 END),
			SUM(CASE WHEN d.deviceStatus = 'PENDING_DECOMMISSIONING' AND d.deleted = false THEN 1 ELSE 0 END),
			SUM(CASE WHEN d.deviceStatus = 'DECOMMISSIONED' AND d.deleted = false THEN 1 ELSE 0 END),
            SUM(CASE WHEN d.deleted = true THEN 1 ELSE 0 END)
        )
        FROM DeviceSnapshot d
        """)
	DeviceSummary getDeviceSummary();

	@Query("""
    SELECT new com.gp.radioanalytics.device.analytics.dto.DevicesByType(
        d.deviceTypeId,
        dt.name,
        COUNT(d)
    )
    FROM DeviceSnapshot d
    JOIN DeviceTypeSnapshot dt ON dt.deviceTypeId = d.deviceTypeId
    WHERE d.deleted = false
    GROUP BY d.deviceTypeId, dt.name
    """)
	List<DevicesByType> getDevicesByType();

	@Query("""
    SELECT new com.gp.radioanalytics.device.analytics.dto.DevicesByDepartment(
        d.departmentId,
        dept.name,
        COUNT(d)
    )
    FROM DeviceSnapshot d
    JOIN DepartmentSnapshot dept ON dept.departmentId = d.departmentId
    WHERE d.deleted = false AND d.departmentId IS NOT NULL
    GROUP BY d.departmentId, dept.name
    """)
	List<DevicesByDepartment> getDevicesByDepartment();
}