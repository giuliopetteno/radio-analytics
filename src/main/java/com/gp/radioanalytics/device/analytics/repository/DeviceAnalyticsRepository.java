package com.gp.radioanalytics.device.analytics.repository;

import com.gp.radioanalytics.device.analytics.dto.DeviceSummary;
import com.gp.radioanalytics.device.analytics.dto.DeviceStatusCount;
import com.gp.radioanalytics.device.snapshot.domain.DeviceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceAnalyticsRepository extends JpaRepository<DeviceSnapshot, Long> {
	@Query("""
        SELECT new com.gp.radioanalytics.device.analytics.dto.DeviceSummary(
            COUNT(d),
            SUM(CASE WHEN d.deleted = false THEN 1 ELSE 0 END),
            SUM(CASE WHEN d.deleted = true THEN 1 ELSE 0 END)
        )
        FROM DeviceSnapshot d
        """)
	DeviceSummary getSummary();

	@Query("""
        SELECT new com.gp.radioanalytics.device.analytics.dto.DeviceStatusCount(
            d.deviceStatus,
            COUNT(d)
        )
        FROM DeviceSnapshot d
        WHERE d.deleted = false
        GROUP BY d.deviceStatus
        """)
	List<DeviceStatusCount> countByStatus();
}