package com.newbarams.ajaja.module.user.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.ChangeReceiveTypeService;
import com.newbarams.ajaja.module.user.application.LogoutService;
import com.newbarams.ajaja.module.user.application.RenewNicknameService;
import com.newbarams.ajaja.module.user.application.SendVerificationEmailService;
import com.newbarams.ajaja.module.user.application.VerifyCertificationService;
import com.newbarams.ajaja.module.user.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final LogoutService logoutService;
	private final RenewNicknameService renewNicknameService;
	private final ChangeReceiveTypeService changeReceiveTypeService;
	private final VerifyCertificationService verifyCertificationService;
	private final SendVerificationEmailService sendVerificationEmailService;

	@Operation(summary = "[토큰 필요] 닉네임 새로고침 API", description = "새로운 랜덤 닉네임을 생성합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 새로운 닉네임으로 변경했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.")
	})
	@PostMapping("/refresh")
	@ResponseStatus(OK)
	public AjajaResponse<String> renewNickname(@UserId Long id) {
		String newNickname = renewNicknameService.renew(id);
		return AjajaResponse.ok(newNickname);
	}

	@Operation(summary = "[토큰 필요] 이메일 검증 요청 API", description = "리마인드 받을 이메일을 검증하기 위해 인증을 요청합니다.", responses = {
		@ApiResponse(responseCode = "204", description = "성공적으로 인증 메일을 전송했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "이메일 인증을 할 수 없습니다. 인증이 완료된 상태라면 기존 리마인드 이메일과 다른 이메일을 입력해야 합니다.")
	})
	@PostMapping("/send-verification")
	@ResponseStatus(NO_CONTENT)
	public void sendVerification(
		@UserId Long id,
		@Valid @RequestBody UserRequest.EmailVerification request
	) {
		sendVerificationEmailService.sendVerification(id, request.email());
	}

	@Operation(summary = "[토큰 필요] 인증 번호 검증 API", description = "발송된 인증 번호를 검증합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 이메일 인증을 완료하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "인증 번호가 일치하지 않습니다."),
	})
	@PostMapping("/verify")
	@ResponseStatus(OK)
	public AjajaResponse<Void> verifyCertification(
		@UserId Long id,
		@Validated @RequestBody UserRequest.Certification request
	) {
		verifyCertificationService.verify(id, request.certification());
		return AjajaResponse.noData();
	}

	@Operation(summary = "[토큰 필요] 로그아웃 API")
	@PostMapping("/logout")
	@ResponseStatus(OK)
	public AjajaResponse<Void> logout(@UserId Long id) {
		logoutService.logout(id);
		return AjajaResponse.noData();
	}

	@Operation(summary = "[토큰 필요] ")
	@PutMapping("/receive")
	@ResponseStatus(OK)
	public AjajaResponse<Void> changeReceiveType(@UserId Long id) {
		changeReceiveTypeService.change(id, "type"); // todo: update
		return AjajaResponse.noData();
	}

	@Operation(summary = "[토큰 필요] 회원 탈퇴 API")
	@DeleteMapping
	@ResponseStatus(OK)
	public AjajaResponse<Void> withdraw(@UserId Long id) {
		// todo: logic
		return AjajaResponse.noData();
	}
}
