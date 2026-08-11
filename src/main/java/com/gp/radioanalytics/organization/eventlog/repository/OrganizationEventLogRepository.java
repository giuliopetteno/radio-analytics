package com.gp.radioanalytics.organization.eventlog.repository;

import com.gp.radioanalytics.organization.eventlog.domain.OrganizationEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationEventLogRepository extends JpaRepository<OrganizationEventLog, Long> {
}
