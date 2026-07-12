package com.gp.radioanalytics.kafka.processedevent.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "processed_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEvent {
	@Id
	@Column(name = "event_id", nullable = false)
	private UUID eventId;

	@Column(name = "entity_type", nullable = false)
	private String entityType;

	@CreationTimestamp
	@Column(name = "processed_at", nullable = false, updatable = false)
	private OffsetDateTime processedAt;
}