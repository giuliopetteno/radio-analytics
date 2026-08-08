package com.gp.radioanalytics.department.repository;

import com.gp.radioanalytics.department.domain.DepartmentSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentSnapshotRepository extends JpaRepository<DepartmentSnapshot, Long> {
}
