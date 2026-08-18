package com.gp.radioanalytics.analytics.dto;

import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.atomic.AtomicLong;

public record TimedSubTask<T>(
	Subtask<T> subTask,
	AtomicLong duration
) {}
