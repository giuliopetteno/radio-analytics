package com.gp.radioanalytics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
	CREATE,
	READ,
	UPDATE,
	STATUS_CHANGED,
	DELETE,
	LOGIN,
	LOGOUT
}
