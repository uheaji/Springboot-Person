package com.cos.person.config;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.person.domain.CommonDto;

// 메모리에 띄우는 법 : @Controller, @RestController, @Component, @Configuration
// @Component는 @Controller가 뜨고 나서 뜬다.
// @Configuration은 설정할 때, 다른 것들은 @Component로 띄우기

@Component
@Aspect
public class BindingAdvice {

	// 함수 : 앞, 뒤 제어 @Around
	// 함수 : 앞(username이 안들어왔으면 내가 강제로 넣어주고 실행) @Before
	// 함수 : 뒤(응답만 관리) @After

	@Around("execution(* com.cos.person.web..*Controller.*(..))")
	public Object validCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String type = proceedingJoinPoint.getSignature().getDeclaringTypeName();
		String method = proceedingJoinPoint.getSignature().getName();

		System.out.println("type : " + type);
		System.out.println("method : " + method);

		Object[] args = proceedingJoinPoint.getArgs();

		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg;

				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}

					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라!
	}

}
