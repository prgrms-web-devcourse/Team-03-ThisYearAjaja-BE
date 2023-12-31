package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawService {
	private final DisablePlanService disablePlanService;
	private final RetrieveUserService retrieveUserService;
	private final DisconnectOauthService disconnectOauthService;

	public void withdraw(Long userId) {
		User user = retrieveUserService.loadExistUserById(userId);
		disconnectOauthService.disconnect(user.getOauthId());
		disablePlanService.disable(userId);
		user.delete();
	}
}
