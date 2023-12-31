package com.newbarams.ajaja.module.user.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.common.RedisBasedTest;
import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.repository.UserRepository;

@RedisBasedTest
class VerifyCertificationServiceTest extends MonkeySupport {
	@Autowired
	private VerifyCertificationService verifyCertificationService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CacheUtil cacheUtil;

	private User user;
	private final String mail = "gmlwh124@naver.com";

	@BeforeEach
	void clear() {
		userRepository.deleteAllInBatch();
		user = userRepository.save(monkey.giveMeBuilder(User.class)
			.set("email", new Email(mail))
			.set("isDeleted", false)
			.sample());
	}

	@Test
	@DisplayName("인증 완료 시 유저 정보가 인증 완료 상태로 변경되어야 한다.")
	void verify_Success_WithUpdatedVerificationStatus() {
		// given
		String certification = RandomCertificationGenerator.generate();
		cacheUtil.saveEmailVerification(user.getId(), mail, certification);

		// when
		verifyCertificationService.verify(user.getId(), certification);

		// then
		User saved = userRepository.findAll().get(0);
		assertThat(saved.getEmail().getRemindEmail()).isEqualTo(mail);
		assertThat(saved.getEmail().isVerified()).isTrue();
	}

	@Test
	@DisplayName("인증 정보를 찾을 수 없으면 인증에 실패한다.")
	void verify_Fail_ByNoCertification() {
		// given

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> verifyCertificationService.verify(user.getId(), "certification"))
			.withMessage(CERTIFICATION_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("인증 번호가 일치하지 않으면 예외를 던져야 한다.")
	void verify_Fail_ByWrongCertification() {
		// given
		String certification = RandomCertificationGenerator.generate();
		cacheUtil.saveEmailVerification(user.getId(), mail, certification);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> verifyCertificationService.verify(user.getId(), "certification"))
			.withMessage(CERTIFICATION_NOT_MATCH.getMessage());
	}
}
