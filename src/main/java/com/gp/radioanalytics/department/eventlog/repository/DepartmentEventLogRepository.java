package com.gp.radioanalytics.department.eventlog.repository;

import com.gp.radioanalytics.department.eventlog.domain.DepartmentEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentEventLogRepository extends JpaRepository<DepartmentEventLog, Long> {
}
