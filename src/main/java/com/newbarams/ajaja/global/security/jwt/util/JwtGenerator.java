package com.newbarams.ajaja.global.security.jwt.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
	private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분
	private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

	private final JwtSecretProvider jwtSecretProvider;
	private final CacheUtil cacheUtil;

	public UserResponse.Token generate(Long userId) {
		final Date now = new Date();
		String accessToken = generateAccessToken(userId, now);
		String refreshToken = generateRefreshToken(now);

		cacheUtil.saveRefreshToken(jwtSecretProvider.cacheKey(userId), refreshToken, REFRESH_TOKEN_VALID_TIME);
		return new UserResponse.Token(accessToken, refreshToken);
	}

	private String generateAccessToken(Long userId, Date now) {
		Date accessTokenExpireIn = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

		return Jwts.builder()
			.claim(jwtSecretProvider.getSignature(), userId)
			.expiration(accessTokenExpireIn)
			.signWith(jwtSecretProvider.getSecretKey())
			.compact();
	}

	private String generateRefreshToken(Date now) {
		Date accessTokenExpireIn = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

		return Jwts.builder()
			.expiration(accessTokenExpireIn)
			.signWith(jwtSecretProvider.getSecretKey())
			.compact();
	}
}
