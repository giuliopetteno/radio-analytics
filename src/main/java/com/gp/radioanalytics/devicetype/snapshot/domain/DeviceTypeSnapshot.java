package com.gp.radioanalytics.devicetype.snapshot.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_type_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceTypeSnapshot {
	@Id
	@Column(name = "device_type_id", nullable = false)
	private Long deviceTypeId;

	@Column(nullable = false)
	private String name;

	private String description;

	@Column(name = "device_type_created_at", nullable = false)
	private OffsetDateTime deviceTypeCreatedAt;

	@Column(name = "device_type_updated_at", nullable = false)
	private OffsetDateTime deviceTypeUpdatedAt;

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
