package com.newbarams.ajaja.module.user.auth.application;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.module.user.application.AuthorizeService;
import com.newbarams.ajaja.module.user.auth.model.AccessToken;
import com.newbarams.ajaja.module.user.auth.model.KakaoTokenRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoAuthorizeService implements AuthorizeService {
	private final KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;
	private final KakaoProperties kakaoProperties;

	@Override
	public AccessToken authorize(String authorizationCode, String redirectUrl) {
		KakaoTokenRequest request = generateRequest(authorizationCode, redirectUrl);
		return kakaoAuthorizeFeignClient.authorize(request);
	}

	private KakaoTokenRequest generateRequest(String authorizationCode, String redirectUrl) {
		return new KakaoTokenRequest(
			kakaoProperties.getClientId(),
			redirectUrl,
			authorizationCode,
			kakaoProperties.getClientSecret()
		);
	}
}
