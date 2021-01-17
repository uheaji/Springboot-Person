package com.cos.person.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cos.person.domain.CommonDto;

import io.sentry.Sentry;

// 메모리에 띄우는 법 : @Controller, @RestController, @Component, @Configuration
// @Component는 @Controller가 뜨고 나서 뜬다.
// @Configuration은 설정할 때, 다른 것들은 @Component로 띄우기


@Component
@Aspect
public class BindingAdvice {
	
	
	private static final Logger log = LoggerFactory.getLogger(BindingAdvice.class);

	 
	// 어떤함수가 언제 몇번 실행됐는지 횟수같은거 로그 남기기
		@Before("execution(* com.cos.person.web..*Controller.*(..))")
		public void testCheck() {
			//request 값 처리 못하나요?
			HttpServletRequest request = 
					((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			System.out.println("주소 : "+request.getRequestURI());
			
			System.out.println("전처리 로그를 남겼습니다.");

		}
		
		@After("execution(* com.cos.person.web..*Controller.*(..))")
		public void testCheck2() {
			
			System.out.println("후처리 로그를 남겼습니다.");

		}

	// 함수 : 앞, 뒤 제어 @Around
	// 함수 : 앞(username이 안들어왔으면 내가 강제로 넣어주고 실행) @Before
	// 함수 : 뒤(응답만 관리) @After
	// 필터는 전처리 하고싶을때 사용하기.

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
				
				// 서비스 : 정상적인 화면 -> 사용자 요청
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						// 로그 레벨 error, warn, info, debug
						// 만약에 로그 레벨 설정을 info로 하면 그 이상(error, warn, info)만 뜬다!
						log.warn(type + "." + method + "() => 필드 : " + error.getField() + ", 메시지 : " + error.getDefaultMessage());
						log.debug(type + "." + method + "() => 필드 : " + error.getField() + ", 메시지 : " + error.getDefaultMessage());
						// 로그 남기는 법 1. DB연결 -> DB남기
						// 로그 남기는 법 2.File file = new File(); 
					}

					return new CommonDto<>(HttpStatus.BAD_REQUEST.value(), errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 함수의 스택을 실행해라!
	}

}
