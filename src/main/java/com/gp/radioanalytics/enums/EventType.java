package com.gp.radioanalytics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
	READ,
	CREATE,
	UPDATE,
	STATUS_CHANGED,
	DELETE
}
