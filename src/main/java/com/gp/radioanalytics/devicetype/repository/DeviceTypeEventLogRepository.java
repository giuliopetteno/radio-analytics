package com.gp.radioanalytics.devicetype.repository;

import com.gp.radioanalytics.devicetype.domain.DeviceTypeEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeEventLogRepository extends JpaRepository<DeviceTypeEventLog, Long> {
}
