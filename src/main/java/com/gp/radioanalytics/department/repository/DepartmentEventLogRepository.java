package com.gp.radioanalytics.department.repository;

import com.gp.radioanalytics.department.domain.DepartmentEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentEventLogRepository extends JpaRepository<DepartmentEventLog, Long> {
}
