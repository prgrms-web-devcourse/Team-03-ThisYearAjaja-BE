package com.newbarams.ajaja.module.feedback.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;
import com.newbarams.ajaja.global.exception.AjajaException;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "feedbacks")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity<Feedback> {
	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feedback_id")
	private Long id;

	@NotNull
	private Long userId;

	@Getter
	@NotNull
	private Long planId;

	@Getter
	@Enumerated(value = EnumType.STRING)
	@NotNull
	private Achieve achieve;

	private boolean isDeleted;

	@Transient // TODO:도메인 객체 분리
	@Embedded
	private ValidDate validDate;

	public Feedback(Long userId, Long planId, Achieve achieve) {
		this.userId = userId;
		this.planId = planId;
		this.achieve = achieve;
		this.isDeleted = false;
		this.validDate = new ValidDate(getCreatedAt());
		this.validateSelf();
	}

	public static Feedback create(Long userId, Long planId) {
		return new Feedback(userId, planId, Achieve.FAIL);
	}

	public void checkDeadline() {
		boolean isInvalidFeedback = validDate.isExpired();

		if (isInvalidFeedback) {
			throw new AjajaException(EXPIRED_FEEDBACK);
		}
	}

	public void updateAchieve(int rate) {
		this.achieve = Achieve.of(rate);
	}

	public int getRate() {
		return this.achieve.getRate();
	}

	public boolean isFeedback() {
		return this.getUpdatedAt().isAfter(this.getCreatedAt());
	}
}
