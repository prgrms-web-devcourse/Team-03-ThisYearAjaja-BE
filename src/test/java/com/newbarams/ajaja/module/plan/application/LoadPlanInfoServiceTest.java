package com.newbarams.ajaja.module.plan.application;

import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.plan.domain.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;

class LoadPlanInfoServiceTest extends MockTestSupport {
	@InjectMocks
	private LoadPlanInfoService loadPlanInfoService;
	@Mock
	private PlanQueryRepository planQueryRepository;
	@Mock
	private CreatePlanResponseService createPlanResponseService;

	@Test
	@DisplayName("처음 계획을 작성한 년도부터 현재 년도까지의 계획들을 조회한다.")
	void getPlanInfo_Success_WithNoException() {
		// given
		PlanInfoResponse.GetPlan planInfo1 = monkey.giveMeBuilder(PlanInfoResponse.GetPlan.class)
			.set("year", 2023).sample();

		PlanInfoResponse.GetPlan planInfo2 = monkey.giveMeBuilder(PlanInfoResponse.GetPlan.class)
			.set("year", 2021).sample();

		int execute = 2023 - 2021 + 1;

		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(List.of(planInfo1, planInfo2));
		given(createPlanResponseService.createPlanInfo(anyInt(), any())).willReturn(null);

		//when
		loadPlanInfoService.loadPlanInfo(1L);

		// then
		then(createPlanResponseService).should(times(execute)).createPlanInfo(anyInt(), any());
	}

	@Test
	@DisplayName("만약 조회된 계획들이 없다면 기본값들을 반환한다.")
	void getNoPlanInfo_Success_WithNoException() {
		// given
		given(planQueryRepository.findAllPlanByUserId(any())).willReturn(Collections.emptyList());

		//when
		List<PlanInfoResponse.GetPlanInfoResponse> planInfoResponses = loadPlanInfoService.loadPlanInfo(1L);

		// then
		Assertions.assertThat(planInfoResponses).isEmpty();
	}
}
