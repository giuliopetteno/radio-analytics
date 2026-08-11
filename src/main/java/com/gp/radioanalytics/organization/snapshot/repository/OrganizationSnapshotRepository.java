package com.gp.radioanalytics.organization.snapshot.repository;

import com.gp.radioanalytics.organization.snapshot.domain.OrganizationSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationSnapshotRepository extends JpaRepository<OrganizationSnapshot, Long> {
}
