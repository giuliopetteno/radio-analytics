package com.gp.radioanalytics.analytics.metrics;

import com.gp.radioanalytics.analytics.enums.ExecutionMode;
import com.gp.radioanalytics.analytics.enums.ExecutionStatus;
import com.gp.radioanalytics.analytics.enums.KpiStatus;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AnalyticsMetrics {
	private final MeterRegistry meterRegistry;

	public void recordKpiExecution(ExecutionMode mode, String kpiName, KpiStatus kpiStatus, Duration duration) {
		meterRegistry
			.timer("analytics.kpi.duration", "mode", mode.name(), "kpi", kpiName, "status", kpiStatus.name())
			.record(duration);
	}

	public void recordAnalyticsExecution(ExecutionMode mode, ExecutionStatus executionStatus, Duration duration) {
		meterRegistry
			.counter("analytics.execution.count", "mode", mode.name(), "status", executionStatus.name())
			.increment();
		meterRegistry
			.timer("analytics.execution.duration", "mode", mode.name())
			.record(duration);
	}
}
