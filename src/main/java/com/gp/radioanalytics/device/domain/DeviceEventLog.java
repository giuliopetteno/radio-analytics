package com.gp.radioanalytics.device.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_event_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEventLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_id", nullable = false, unique = true)
	private UUID eventId;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@Column(name = "device_id", nullable = false)
	private Long deviceId;

	@Column(nullable = false)
	private String name;

	@Column(name = "device_type_id", nullable = false)
	private Long deviceTypeId;

	@Column(name = "serial_number", nullable = false)
	private String serialNumber;

	private String description;

	@Column(name = "installation_date", nullable = false)
	private LocalDate installationDate;

	@Column(name = "device_status", nullable = false)
	private String deviceStatus;

	@Column(name = "decommission_date")
	private LocalDate decommissionDate;

	@Column(name = "organization_id")
	private Long organizationId;

	@Column(name = "department_id")
	private Long departmentId;

	@Column(name = "device_created_at", nullable = false)
	private OffsetDateTime deviceCreatedAt;

	@Column(name = "device_updated_at", nullable = false)
	private OffsetDateTime deviceUpdatedAt;

	@Column(name = "produced_at", nullable = false)
	private OffsetDateTime producedAt;

	@CreationTimestamp
	@Column(name = "consumed_at", nullable = false, updatable = false)
	private OffsetDateTime consumedAt;
}
