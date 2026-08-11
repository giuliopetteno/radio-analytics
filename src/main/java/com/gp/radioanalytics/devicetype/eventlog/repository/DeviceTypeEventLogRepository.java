package com.gp.radioanalytics.devicetype.eventlog.repository;

import com.gp.radioanalytics.devicetype.eventlog.domain.DeviceTypeEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeEventLogRepository extends JpaRepository<DeviceTypeEventLog, Long> {
}
