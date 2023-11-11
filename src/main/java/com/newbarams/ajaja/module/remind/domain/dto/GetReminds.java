package com.newbarams.ajaja.module.remind.domain.dto;

import java.sql.Timestamp;
import java.util.List;

public sealed interface GetReminds permits GetReminds.CommonResponse, GetReminds.Response {

	record CommonResponse(
		String remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<Response> responses) implements GetReminds {
	}

	record Response(
		int index,
		Long feedbackId,
		String remindMessage,
		boolean isFeedback,
		int rate,
		boolean isExpired,
		Timestamp deadLine
	) implements GetReminds {
	}
}
