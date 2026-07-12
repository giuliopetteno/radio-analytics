package com.gp.radioanalytics.kafka.processedevent.repository;

import com.gp.radioanalytics.kafka.processedevent.domain.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> {
}
