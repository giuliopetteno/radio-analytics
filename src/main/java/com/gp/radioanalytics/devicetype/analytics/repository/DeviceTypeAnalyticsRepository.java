package com.gp.radioanalytics.devicetype.analytics.repository;

import com.gp.radioanalytics.analytics.dto.MonthlyEventsCount;
import com.gp.radioanalytics.analytics.dto.Summary;
import com.gp.radioanalytics.devicetype.snapshot.domain.DeviceTypeSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceTypeAnalyticsRepository extends JpaRepository<DeviceTypeSnapshot, Long> {
	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.Summary(
        SUM(CASE WHEN dts.deleted = false THEN 1 ELSE 0 END),
        SUM(CASE WHEN dts.deleted = false AND EXISTS (
            SELECT 1 FROM DeviceSnapshot ds WHERE ds.deviceTypeId = dts.deviceTypeId AND ds.deleted = false
        ) THEN 1 ELSE 0 END),
        SUM(CASE WHEN dts.deleted = false AND NOT EXISTS (
            SELECT 1 FROM DeviceSnapshot ds WHERE ds.deviceTypeId = dts.deviceTypeId AND ds.deleted = false
        ) THEN 1 ELSE 0 END),
    	SUM(CASE WHEN dts.deleted = true THEN 1 ELSE 0 END)
    )
    FROM DeviceTypeSnapshot dts
    """)
	Summary getDeviceTypeSummary();

	@Query("""
    SELECT new com.gp.radioanalytics.analytics.dto.MonthlyEventsCount(
        YEAR(dtel.producedAt),
        MONTH(dtel.producedAt),
        dtel.eventType,
        COUNT(dtel)
    )
    FROM DeviceTypeEventLog dtel
    GROUP BY YEAR(dtel.producedAt), MONTH(dtel.producedAt), dtel.eventType
    ORDER BY YEAR(dtel.producedAt), MONTH(dtel.producedAt), dtel.eventType
    """)
	List<MonthlyEventsCount> getDeviceTypeEventsTrend();
}
