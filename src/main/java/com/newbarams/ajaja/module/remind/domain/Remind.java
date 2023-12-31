package com.newbarams.ajaja.module.remind.domain;

import java.time.Instant;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reminds")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Remind extends BaseEntity<Remind> {
	public enum Type {
		PLAN,
		AJAJA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "remind_id")
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long planId;

	@Embedded
	private Info info;
	private Period period;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "remind_type")
	private Type type;

	private boolean isDeleted;

	Remind(Long userId, Long planId, Info info, Period period, Type type, boolean isDeleted) {
		this.userId = userId;
		this.planId = planId;
		this.info = info;
		this.period = period;
		this.type = type;
		this.isDeleted = isDeleted;
	}

	public static Remind plan(Long userId, Long planId, Info info, Period period) {
		return new Remind(userId, planId, info, period, Type.PLAN, false);
	}

	public static Remind ajaja(Long userId, Long planId, Info info, Period period) {
		return new Remind(userId, planId, info, period, Type.AJAJA, false);
	}

	public boolean isExpired() {
		return this.period.isExpired();
	}

	public Instant getStart() {
		return this.period.getStarts();
	}

	public Instant getEnd() {
		return this.period.getEnds();
	}
}
