package com.gp.radioanalytics.organization.repository;

import com.gp.radioanalytics.organization.domain.OrganizationEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationEventLogRepository extends JpaRepository<OrganizationEventLog, Long> {
}
