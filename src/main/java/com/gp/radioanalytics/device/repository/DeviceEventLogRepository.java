package com.gp.radioanalytics.device.repository;

import com.gp.radioanalytics.device.domain.DeviceEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceEventLogRepository extends JpaRepository<DeviceEventLog, Long> {
}
