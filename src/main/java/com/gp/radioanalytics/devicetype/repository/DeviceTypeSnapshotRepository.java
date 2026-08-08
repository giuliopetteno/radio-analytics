package com.gp.radioanalytics.devicetype.repository;

import com.gp.radioanalytics.devicetype.domain.DeviceTypeSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeSnapshotRepository extends JpaRepository<DeviceTypeSnapshot, Long> {
}
