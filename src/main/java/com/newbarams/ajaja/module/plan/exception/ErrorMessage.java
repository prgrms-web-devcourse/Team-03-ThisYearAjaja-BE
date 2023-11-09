package com.newbarams.ajaja.module.plan.exception;

public enum ErrorMessage {
	NOT_FOUND_PLAN("존재하지 않는 계획입니다.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
