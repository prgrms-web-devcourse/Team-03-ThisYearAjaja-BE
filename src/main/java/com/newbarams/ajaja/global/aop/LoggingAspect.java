package com.newbarams.ajaja.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
	private static final String CONTROLLER_LOGGING_CONDITION = """
		(within(@org.springframework.stereotype.Controller *)
		|| within(@org.springframework.web.bind.annotation.RestController *))
		&& execution(public * *(..))
		""";

	@Around(CONTROLLER_LOGGING_CONDITION)
	public Object executeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();

		ServletRequestAttributes requestAttributes
			= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes).getRequest();

		String controllerLog =
			"[API] Called : " + request.getMethod() + " " + request.getRequestURI()
				+ ",Processed : " + stopWatch.getTotalTimeMillis() + "ms";

		log.info(controllerLog);

		return result;
	}
}
