package com.gp.radioanalytics.devicetype.snapshot.repository;

import com.gp.radioanalytics.devicetype.snapshot.domain.DeviceTypeSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceTypeSnapshotRepository extends JpaRepository<DeviceTypeSnapshot, Long> {
}
