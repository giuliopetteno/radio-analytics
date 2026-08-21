package com.gp.radioanalytics.department.snapshot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "department_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSnapshot {
	@Id
	@Column(name = "department_id", nullable = false)
	private Long departmentId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;

	private String description;

	@Column(name = "organization_id")
	private Long organizationId;

	@Column(name = "parent_department_id")
	private Long parentDepartmentId;

	@Column(name = "department_created_at", nullable = false)
	private OffsetDateTime departmentCreatedAt;

	@Column(name = "department_updated_at", nullable = false)
	private OffsetDateTime departmentUpdatedAt;

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	@Column(name = "deleted_at")
	private OffsetDateTime deletedAt;

	@Column(name = "last_event_id", nullable = false)
	private UUID lastEventId;

	@Column(name = "last_event_type", nullable = false)
	private String lastEventType;

	@Column(name = "last_event_produced_at", nullable = false)
	private OffsetDateTime lastEventProducedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;
}
