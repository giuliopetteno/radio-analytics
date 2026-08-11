package com.gp.radioanalytics.device.eventlog.repository;

import com.gp.radioanalytics.device.eventlog.domain.DeviceEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceEventLogRepository extends JpaRepository<DeviceEventLog, Long> {
}
