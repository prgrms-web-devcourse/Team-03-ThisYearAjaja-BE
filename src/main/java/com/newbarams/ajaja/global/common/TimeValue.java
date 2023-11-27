package com.newbarams.ajaja.global.common;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeValue {
	private final Instant instant;
	private final ZonedDateTime zonedDateTime;

	public TimeValue(Instant instant) {
		this.instant = instant;
		zonedDateTime = instant.atZone(ZoneId.of("Asia/Seoul"));
	}

	public int getMonth() {
		return zonedDateTime.getMonthValue();
	}

	public int getDate() {
		return zonedDateTime.getDayOfMonth();
	}

	public Timestamp toTimeStampOf() {
		return Timestamp.valueOf(instant.toString());
	}

	public LocalDateTime toLocalDateTimeOf() {
		return zonedDateTime.toLocalDateTime();
	}
}