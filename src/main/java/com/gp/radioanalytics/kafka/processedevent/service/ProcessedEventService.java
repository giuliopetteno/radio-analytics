package com.gp.radioanalytics.kafka.processedevent.service;

import com.gp.radioanalytics.kafka.processedevent.domain.ProcessedEvent;
import com.gp.radioanalytics.kafka.processedevent.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessedEventService {

	private final ProcessedEventRepository processedEventRepository;

	public boolean isProcessed(UUID eventId) {
		return processedEventRepository.existsById(eventId);
	}

	public void markAsProcessed(UUID eventId, String entityType) {
		processedEventRepository.save(
			ProcessedEvent.builder()
				.eventId(eventId)
				.entityType(entityType)
				.build()
		);
	}
}
