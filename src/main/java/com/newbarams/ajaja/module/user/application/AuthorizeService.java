package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.user.auth.model.AccessToken;

@Service
public interface AuthorizeService {
	/**
	 * @param authorizationCode Key for authorize which generated by Oauth
	 * @return Access token
	 */
	AccessToken authorize(String authorizationCode);
}