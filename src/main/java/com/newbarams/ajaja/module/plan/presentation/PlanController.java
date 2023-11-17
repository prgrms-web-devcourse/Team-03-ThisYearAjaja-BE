package com.newbarams.ajaja.module.plan.presentation;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.GetPlanAchieveService;
import com.newbarams.ajaja.module.plan.application.GetPlanInfoService;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "plan")
@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {
	private final CreatePlanService createPlanService;
	private final LoadPlanService getPlanService;
	private final DeletePlanService deletePlanService;
	private final GetPlanAchieveService getPlanAchieveService;
	private final UpdatePlanService updatePlanService;
	private final GetPlanInfoService getPlanInfoService;

	@Operation(summary = "[토큰 필요] 계획 생성 API")
	@PostMapping
	@ResponseStatus(CREATED)
	public AjajaResponse<PlanResponse.Create> createPlan(
		@UserId Long userId,
		@RequestBody PlanRequest.Create request,
		@RequestHeader(name = "Date") String date
	) {
		PlanResponse.Create response = createPlanService.create(userId, request, date);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "계획 단건 조회 API")
	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.GetOne> getPlan(@PathVariable Long id) {
		PlanResponse.GetOne response = getPlanService.loadById(id);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "[토큰 필요] 계획 삭제 API")
	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse deletePlan(
		@PathVariable Long id,
		@UserId Long userId,
		@RequestHeader(name = "Date") String date
	) {
		deletePlanService.delete(id, userId, date);

		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "[토큰 필요] 특정 목표 달성률 조회 API")
	@GetMapping("/{planId}/feedbacks")
	@ResponseStatus(OK)
	public AjajaResponse<Integer> getPlanAchieve(@PathVariable Long planId) {
		int totalAchieve = getPlanAchieveService.calculatePlanAchieve(planId);

		return new AjajaResponse<>(true, totalAchieve);
	}

	@Operation(summary = "[토큰 필요] 계획 공개 여부 변경 API")
	@PutMapping("/{id}/public")
	@ResponseStatus(OK)
	public AjajaResponse updatePlanPublicStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updatePublicStatus(id, userId);

		return new AjajaResponse(true, null);
	}

	@Operation(summary = "[토큰 필요] 계획 리마인드 알림 여부 변경 API")
	@PutMapping("/{id}/remindable")
	@ResponseStatus(OK)
	public AjajaResponse updatePlanRemindStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updateRemindStatus(id, userId);

		return new AjajaResponse(true, null);
	}

	@Operation(summary = "[토큰 필요] 응원메시지 알림 여부 변경 API")
	@PutMapping("/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse updatePlanAjajaStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updateAjajaStatus(id, userId);

		return new AjajaResponse(true, null);
	}

	@Operation(summary = "[토큰 필요] 계획 수정 API")
	@PutMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Create> updatePlan(
		@PathVariable Long id,
		@UserId Long userId,
		@RequestBody PlanRequest.Update request,
		@RequestHeader(name = "Date") String date
	) {
		PlanResponse.Create updated = updatePlanService.update(id, userId, request, date);

		return new AjajaResponse<>(true, updated);
	}

	@Operation(description = "[토큰 필요] 메인페이지 목표 조회 API")
	@GetMapping("/main/{userId}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanInfoResponse.GetPlanInfoResponse> getPlanInfo(@PathVariable Long userId) {
		PlanInfoResponse.GetPlanInfoResponse getPlanInfo = getPlanInfoService.loadPlanInfo(userId);

		return new AjajaResponse<>(true, getPlanInfo);
	}

	@Operation(summary = "계획 전체 조회 API")
	@GetMapping
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans(@ModelAttribute PlanRequest.GetAll request) {
		List<PlanResponse.GetAll> responses = getPlanService.loadAllPlans(request);

		return new AjajaResponse<>(true, responses);
	}
}
