package com.newbarams.ajaja.module.user.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo extends SelfValidating<OauthInfo> {
	@NotNull
	private Long oauthId;

	@Enumerated(EnumType.STRING)
	private OauthProvider oauthProvider;

	public OauthInfo(Long oauthId, OauthProvider oauthProvider) {
		this.oauthId = oauthId;
		this.oauthProvider = oauthProvider;
		this.validateSelf();
	}

	public static OauthInfo kakao(Long oauthId) {
		return new OauthInfo(oauthId, OauthProvider.KAKAO);
	}
}
