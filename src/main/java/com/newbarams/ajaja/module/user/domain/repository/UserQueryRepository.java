package com.newbarams.ajaja.module.user.domain.repository;

import static com.newbarams.ajaja.module.user.domain.QUser.*;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.dto.QUserResponse_MyPage;
import com.newbarams.ajaja.module.user.dto.UserResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
	private final JPAQueryFactory queryFactory;

	public UserResponse.MyPage findUserInfoById(Long userId) {
		return queryFactory.select(new QUserResponse_MyPage(
				user.nickname.nickname,
				user.email.email,
				user.email.remindEmail,
				user.email.isVerified,
				user.receiveType.stringValue().toLowerCase()))
			.from(user)
			.where(user.id.eq(userId))
			.fetchOne();
	}
}
