package com.gp.radioanalytics.devicetype.analytics.repository;

import com.gp.radioanalytics.devicetype.analytics.dto.DeviceTypeSummary;
import com.gp.radioanalytics.devicetype.snapshot.domain.DeviceTypeSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceTypeAnalyticsRepository extends JpaRepository<DeviceTypeSnapshot, Long> {
	@Query("""
    SELECT new com.gp.radioanalytics.devicetype.analytics.dto.DeviceTypeSummary(
        COUNT(dt),
        SUM(CASE WHEN EXISTS (
            SELECT 1 FROM DeviceSnapshot d WHERE d.deviceTypeId = dt.deviceTypeId AND d.deleted = false
        ) THEN 1 ELSE 0 END),
        SUM(CASE WHEN NOT EXISTS (
            SELECT 1 FROM DeviceSnapshot d WHERE d.deviceTypeId = dt.deviceTypeId AND d.deleted = false
        ) THEN 1 ELSE 0 END)
    )
    FROM DeviceTypeSnapshot dt
    WHERE dt.deleted = false
    """)
	DeviceTypeSummary getDeviceTypeSummary();
}
