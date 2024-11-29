package com.springdata.jpa.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class ExecutionTimeAspect {

	@Around("execution(* com.springdata.jpa.controller.AuthorController.*(..))")
	public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long endTime = System.currentTimeMillis();
		long timeTaken = endTime - startTime;


		if (result instanceof ResponseEntity) {
			ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
			Object body = responseEntity.getBody();
			if (body instanceof Map) {
				((Map<String, Object>) body).put("timeTaken", timeTaken + " ms");
			}
			return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
		}

		return result;
	}
}