package com.newbarams.ajaja.global.common.exception;

import com.newbarams.ajaja.global.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class AjajaException extends RuntimeException {
	private final ErrorCode errorCode;

	public AjajaException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	private AjajaException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public AjajaException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public static AjajaException withId(Long id, ErrorCode errorCode) {
		String message = errorCode.getMessage() + " Wrong with Id :" + id;
		return new AjajaException(message, errorCode);
	}

	public int getHttpStatus() {
		return errorCode.getHttpStatus().value();
	}
}
