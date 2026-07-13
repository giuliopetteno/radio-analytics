package com.gp.radioanalytics.device.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "device_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceSnapshot {
	@Id
	@Column(name = "device_id", nullable = false)
	private Long deviceId;

	@Column(nullable = false)
	private String name;

	@Column(name = "device_type_id", nullable = false)
	private Long deviceTypeId;

	@Column(name = "serial_number", nullable = false)
	private String serialNumber;

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
