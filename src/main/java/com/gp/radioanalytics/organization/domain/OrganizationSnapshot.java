package com.gp.radioanalytics.organization.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "organization_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSnapshot {
	@Id
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

	@Column(name = "deleted", nullable = false)
	private boolean deleted = false;

	@Column(name = "deleted_at")
	private OffsetDateTime deletedAt;

	@Column(name = "last_event_id", nullable = false)
	private UUID lastEventId;

	@Column(name = "last_event_type", nullable = false)
	private String lastEventType;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;
}
