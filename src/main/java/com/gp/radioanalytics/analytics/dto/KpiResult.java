package com.gp.radioanalytics.analytics.dto;

import com.gp.radioanalytics.analytics.enums.KpiRequirement;
import com.gp.radioanalytics.analytics.enums.KpiStatus;

public record KpiResult<T>(
	T result,
	KpiStatus status,
	KpiRequirement requirement
) {}
