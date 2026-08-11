package com.gp.radioanalytics.organization.eventlog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization_event_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationEventLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false, unique = true)
	private UUID eventId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@Column(name = "organization_id", nullable = false)
	private Long organizationId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;

	private String description;

	@Column(name = "organization_created_at", nullable = false)
	private OffsetDateTime organizationCreatedAt;

	@Column(name = "organization_updated_at", nullable = false)
	private OffsetDateTime organizationUpdatedAt;

	@Column(name = "produced_at", nullable = false)
	private OffsetDateTime producedAt;

	@CreationTimestamp
	@Column(name = "consumed_at", nullable = false, updatable = false)
	private OffsetDateTime consumedAt;
}
