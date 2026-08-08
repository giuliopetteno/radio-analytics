package com.gp.radioanalytics.organization.repository;

import com.gp.radioanalytics.organization.domain.OrganizationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationSnapshotRepository extends JpaRepository<OrganizationSnapshot, Long> {
}
