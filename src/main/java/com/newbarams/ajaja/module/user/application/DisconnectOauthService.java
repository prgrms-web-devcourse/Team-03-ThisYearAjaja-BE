package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

@Service
public interface DisconnectOauthService {
	void disconnect(Long oauthId);
}
