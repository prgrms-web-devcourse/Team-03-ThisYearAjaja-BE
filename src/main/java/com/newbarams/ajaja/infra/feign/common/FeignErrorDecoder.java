package com.newbarams.ajaja.infra.feign.common;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.Date;

import com.newbarams.ajaja.global.exception.AjajaException;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
	private static final int SERVER_UNAVAILABLE = 503;
	private static final int TOO_MANY_REQUEST = 429;

	private final FeignResponseEncoder feignResponseEncoder;

	@Override
	public Exception decode(String methodKey, Response response) {
		return switch (response.status()) {
			case TOO_MANY_REQUEST, SERVER_UNAVAILABLE -> generateRetryableException(response);
			default -> decodeFeignException(response);
		};
	}

	private Exception decodeFeignException(Response response) {
		String requestBody = feignResponseEncoder.encodeRequestBody(response.request());
		String responseBody = feignResponseEncoder.encodeResponseBody(response.body());

		log.info("[Feign] API Request Fail To {} By {}.", response.request().url(), response.reason());
		log.info("[Feign] Failed Request Body : {}", requestBody);
		log.info("[Feign] Response From API : {}", responseBody);

		return new AjajaException(EXTERNAL_API_FAIL);
	}

	private RuntimeException generateRetryableException(Response response) {
		log.info("[Feign] Retrying API Request To {}", response.request().url());

		return new RetryableException(
			response.status(),
			response.reason(),
			response.request().httpMethod(),
			new Date(System.currentTimeMillis()),
			response.request()
		);
	}
}
