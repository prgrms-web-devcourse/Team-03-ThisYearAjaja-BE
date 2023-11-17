package com.newbarams.ajaja.global.common.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// 400
	BEAN_VALIDATE_FAIL_EXCEPTION(BAD_REQUEST, "올바르지 않은 데이터입니다."),
	NOT_FOUND_FEEDBACK(BAD_REQUEST, "피드백 정보가 존재하지 않습니다."),
	NOT_FOUND_PLAN(BAD_REQUEST, "계획 정보가 존재하지 않습니다."),
	INVALID_BEARER_FORMAT(BAD_REQUEST, "유효한 Bearer 토큰의 형식이 아닙니다."),
	INVALID_SIGNATURE(BAD_REQUEST, "잘못된 서명입니다."),
	INVALID_TOKEN(BAD_REQUEST, "잘못된 토큰입니다."),
	EMPTY_TOKEN(BAD_REQUEST, "비어있는 토큰입니다."),
	EXPIRED_TOKEN(BAD_REQUEST, "만료된 토큰입니다."),
	UNSUPPORTED_TOKEN(BAD_REQUEST, "지원하지 않는 토큰입니다."),
	TOKEN_NOT_MATCH(BAD_REQUEST, "일치하지 않는 토큰입니다."),
	INVALID_USER_ACCESS(BAD_REQUEST, "잘못된 유저의 접근입니다."),
	INVALID_UPDATABLE_DATE(BAD_REQUEST, "변경 가능한 기간이 아닙니다."),

	// 404
	USER_NOT_FOUND(NOT_FOUND, "사용자가 존재하지 않습니다."),
	CERTIFICATION_NOT_FOUND(NOT_FOUND, "인증 요청 시간이 초과하였거나 인증을 요청한 적이 없습니다."),
	NEVER_LOGIN(NOT_FOUND, "로그인한 이력을 찾을 수 없습니다. 다시 로그인 해주세요."),

	// 409
	EXPIRED_FEEDBACK(CONFLICT, "평가 기간이 지났습니다."),
	UNABLE_TO_VERIFY_EMAIL(CONFLICT, "이메일 인증을 할 수 없습니다. 인증이 완료된 상태라면 기존 리마인드 이메일과 다른 이메일을 입력해야 합니다."),
	CERTIFICATION_NOT_MATCH(CONFLICT, "인증 번호가 일치하지 않습니다."),

	// 500
	AJAJA_SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의바랍니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
