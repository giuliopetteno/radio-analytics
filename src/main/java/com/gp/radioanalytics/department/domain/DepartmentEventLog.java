package com.gp.radioanalytics.department.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "department_event_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentEventLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false, unique = true)
	private UUID eventId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

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

	@Column(name = "produced_at", nullable = false)
	private OffsetDateTime producedAt;

	@CreationTimestamp
	@Column(name = "consumed_at", nullable = false, updatable = false)
	private OffsetDateTime consumedAt;
}
