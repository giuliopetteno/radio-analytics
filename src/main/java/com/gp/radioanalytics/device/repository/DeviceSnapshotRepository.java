package com.gp.radioanalytics.device.repository;

import com.gp.radioanalytics.device.domain.DeviceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSnapshotRepository extends JpaRepository<DeviceSnapshot, Long> {
}
