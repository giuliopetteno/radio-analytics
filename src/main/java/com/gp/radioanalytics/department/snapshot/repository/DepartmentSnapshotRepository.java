package com.gp.radioanalytics.department.snapshot.repository;

import com.gp.radioanalytics.department.snapshot.domain.DepartmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentSnapshotRepository extends JpaRepository<DepartmentSnapshot, Long> {
}
