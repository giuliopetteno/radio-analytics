package com.gp.radioanalytics.device.snapshot.repository;

import com.gp.radioanalytics.device.snapshot.domain.DeviceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSnapshotRepository extends JpaRepository<DeviceSnapshot, Long> {
}
