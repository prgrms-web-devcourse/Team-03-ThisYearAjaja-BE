package com.newbarams.ajaja.module.plan.application;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.feedback.domain.repository.FeedbackRepository;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@ExtendWith(MockitoExtension.class)
class GetPlanAchieveServiceTest {
	@InjectMocks
	private GetPlanAchieveService getPlanAchieveService;

	@Mock
	private FeedbackRepository feedbackRepository;

	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	@DisplayName("조회하고 싶은 계획의 피드백을 통틀어서 달성률의 평균을 매긴다.")
	void getTotalAchieve_Success_WithNoException() {
		// given
		List<Feedback> feedbackList = sut.giveMe(Feedback.class, 2);

		int calculatedAchieve =
			(feedbackList.get(0).getAchieve().getRate() + feedbackList.get(1).getAchieve().getRate()) / 2;

		// when
		given(feedbackRepository.findAllByPlanIdIdAndCreatedYear(any())).willReturn(feedbackList);

		// then
		int totalAchieve = getPlanAchieveService.calculatePlanAchieve(1L);

		Assertions.assertThat(totalAchieve).isEqualTo(calculatedAchieve);
	}

	@Test
	@DisplayName("만약 계획의 평가된 피드백이 없을 경우 점수는 0이 나온다.")
	void getEmptyAchieve_Success_WithNoException() {
		// given
		List<Feedback> feedbackList = Collections.emptyList();

		// when
		given(feedbackRepository.findAllByPlanIdIdAndCreatedYear(any())).willReturn(feedbackList);

		// then
		int totalAchieve = getPlanAchieveService.calculatePlanAchieve(1L);

		Assertions.assertThat(totalAchieve).isZero();
	}
}