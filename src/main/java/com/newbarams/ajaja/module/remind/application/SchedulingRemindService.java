package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.feedback.application.CreateFeedbackService;
import com.newbarams.ajaja.module.plan.domain.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindMessageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulingRemindService {
	private final CreateRemindService createRemindService;
	private final SendPlanRemindService sendPlanRemindService;
	private final CreateFeedbackService createFeedbackService;
	private final PlanQueryRepository planQueryRepository;

	@Scheduled(cron = "0 0 9 * * *")
	public void scheduleMorningRemind() {
		String remindTime = "MORNING";

		sendRemindsOnScheduledTime(remindTime);
	}

	@Scheduled(cron = "0 0 13 * * *")
	public void scheduleAfternoonRemind() {
		String remindTime = "AFTERNOON";

		sendRemindsOnScheduledTime(remindTime);
	}

	@Scheduled(cron = "0 0 22 * * *")
	public void scheduleEveningRemind() {
		String remindTime = "EVENING";

		sendRemindsOnScheduledTime(remindTime);
	}

	private void sendRemindsOnScheduledTime(String remindTime) {
		List<RemindMessageInfo> remindablePlans = planQueryRepository.findAllRemindablePlan(remindTime);
		sendEmail(remindablePlans); // todo : method 수정
	}

	private void sendEmail(List<RemindMessageInfo> remindMessageInfos) {
		for (RemindMessageInfo remindInfo : remindMessageInfos) {
			createFeedbackService.createFeedback(remindInfo.getUserId(), remindInfo.getPlanId());

			sendPlanRemindService.send(
				remindInfo.getEmail(),
				remindInfo.getTitle(),
				remindInfo.getMessage(),
				remindInfo.getPlanId()
			);

			createRemindService.createRemind(remindInfo.getUserId(), remindInfo.getPlanId(), remindInfo.getMessage(),
				remindInfo.getInfo());
		}
	}
}
