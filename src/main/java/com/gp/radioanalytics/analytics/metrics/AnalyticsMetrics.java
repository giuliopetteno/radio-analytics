package com.gp.radioanalytics.analytics.metrics;

import com.gp.radioanalytics.analytics.enums.AnalyticsExecutionMode;
import com.gp.radioanalytics.analytics.enums.AnalyticsStatus;
import com.gp.radioanalytics.analytics.enums.AnalyticsTaskStatus;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AnalyticsMetrics {
	private final MeterRegistry meterRegistry;

	public void recordTaskExecution(String taskName, AnalyticsTaskStatus status, Duration duration) {
		meterRegistry
			.timer("analytics.kpi.duration", "kpi", taskName, "status", status.name())
			.record(duration);
	}

	public void recordAnalyticsExecution(AnalyticsExecutionMode mode, AnalyticsStatus status, Duration duration) {
		meterRegistry
			.counter("analytics.execution.count", "mode", mode.name(), "status", status.name())
			.increment();
		meterRegistry
			.timer("analytics.execution.duration", "mode", mode.name())
			.record(duration);
	}
}
