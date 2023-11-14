package com.newbarams.ajaja.module.remind.application;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.repository.RemindQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulingRemindService {
	private final RemindQueryRepository remindQueryRepository;
	private final SendEmailRemindService sendEmailRemindService;

	@Scheduled(cron = "0 0 9 * * *")
	public void scheduleMorningRemind() {
		int remindHour = 9;

		executeRemindService(remindHour);
	}

	@Scheduled(cron = "0 0 13 * * *")
	public void scheduleAfternoonRemind() {
		int remindHour = 13;

		executeRemindService(remindHour);
	}

	@Scheduled(cron = "0 0 22 * * *")
	public void scheduleEveningRemind() {
		int remindHour = 22;

		executeRemindService(remindHour);
	}

	private void executeRemindService(int remindHour) {
		List<Remind> reminds = findReminds(remindHour);

		sendEmail(reminds);
	}

	private List<Remind> findReminds(int remindHour) {
		return remindQueryRepository.findRemindByHour(remindHour);
	}

	private void sendEmail(List<Remind> reminds) {
		for (Remind remind : reminds) {
			sendEmailRemindService.send("yamsang2002@naver.com", remind.getInfo().getContent(), remind.getPlanId());
		}
	}
}
