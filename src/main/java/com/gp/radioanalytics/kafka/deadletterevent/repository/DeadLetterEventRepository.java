package com.gp.radioanalytics.kafka.deadletterevent.repository;

import com.gp.radioanalytics.kafka.deadletterevent.domain.DeadLetterEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeadLetterEventRepository extends JpaRepository<DeadLetterEvent, Long> {
}
