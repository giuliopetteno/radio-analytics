package com.gp.radioanalytics.devicetype.eventlog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_type_event_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceTypeEventLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false, unique = true)
	private UUID eventId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@Column(name = "device_type_id", nullable = false)
	private Long deviceTypeId;

	@Column(nullable = false)
	private String name;

	private String description;

	@Column(name = "device_type_created_at", nullable = false)
	private OffsetDateTime deviceTypeCreatedAt;

	@Column(name = "device_type_updated_at", nullable = false)
	private OffsetDateTime deviceTypeUpdatedAt;

	@Column(name = "produced_at", nullable = false)
	private OffsetDateTime producedAt;

	@CreationTimestamp
	@Column(name = "consumed_at", nullable = false, updatable = false)
	private OffsetDateTime consumedAt;
}
