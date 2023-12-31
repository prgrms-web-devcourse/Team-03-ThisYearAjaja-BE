package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.model.Profile;
import com.newbarams.ajaja.module.user.domain.OauthInfo;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.repository.UserRepository;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
	private final AuthorizeService authorizeService;
	private final GetProfileService getProfileService;
	private final UserRepository userRepository;
	private final JwtGenerator jwtGenerator;

	public UserResponse.Token login(String authorizationCode, String redirectUri) {
		AccessToken accessToken = authorizeService.authorize(authorizationCode, redirectUri);
		Profile profile = getProfileService.getProfile(accessToken.getContent());
		User user = findUserOrCreateIfNotExists(profile.getEmail(), profile.getInfo());
		return jwtGenerator.generate(user.getId());
	}

	private User findUserOrCreateIfNotExists(String email, OauthInfo oauthInfo) {
		return userRepository.findByEmail_Email(email)
			.orElseGet(() -> createUser(email, oauthInfo));
	}

	private User createUser(String email, OauthInfo oauthInfo) {
		User user = new User(RandomNicknameGenerator.generate(), email, oauthInfo);
		return userRepository.save(user);
	}
}
