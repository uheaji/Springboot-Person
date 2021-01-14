package com.cos.person.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.person.domain.CommonDto;
import com.cos.person.domain.JoinReqDto;
import com.cos.person.domain.UpdateReqDto;
import com.cos.person.domain.User;
import com.cos.person.domain.UserRepository;


@RestController
public class UserController {
	
	private UserRepository userRepository;
	
	// DI = 의존성 주입
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	// http://localhost:8080/user
	// Rest Api는 주소에 모델명만 적기!!
	@GetMapping("/user")
	public CommonDto<List<User>> findAll() {
//		UserRepository userRepository = new UserRepository();
//		userRepository.findAll();
		System.out.println("findAll()");
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.findAll()); // MessageConverter가 동작 된다. => 자바오브젝트를 Json 문자열로 바꿔준다.
	}
	
	// http://localhost:8080/user/1
	// 주소로 들어오는 모든값은 String -> @PathVariable
	@GetMapping("/user/{id}")
	public CommonDto<User> findById(@PathVariable int id) {
		System.out.println("findById() : id : " + id );
		return new CommonDto<>(HttpStatus.OK.value(), userRepository.findById(id));
	}
	
	// CORS 정책
	@CrossOrigin
	// http://localhost:8080/user
	@PostMapping("/user")
	// x-www-form-urlencded => request.getParameter()
	// text/plain => @RequestBody 어노테이션 : 버퍼로 바로읽어준다.
	// application/json => @ResponseBodt 어노테이션 + 오브젝트로 받기
	public CommonDto<String> save(@RequestBody  JoinReqDto dto) {
		System.out.println("save()");
		System.out.println("user : "+ dto);
		userRepository.save(dto);

//		System.out.println("data : " +data);
//		System.out.println("username : " + username);
//		System.out.println("password : " + password);
//		System.out.println("phone : " + phone);
		
		// ok로 리턴하는 건 위험하다. -> ResponseEntity
		return new CommonDto<>(HttpStatus.CREATED.value(),"ok");
	}
	
	// http://localhost:8080/user/1
	// findById()와 주소가 같지만 메소드가 다르다.
	@DeleteMapping("/user/{id}")
	public CommonDto delete(@PathVariable int id) {
		System.out.println("delete()");
		userRepository.delete(id);
		return new CommonDto<>(HttpStatus.OK.value());
	}
	
	// http://localhost:8080/user/1
	@PutMapping("/user/{id}")
	public CommonDto update(@PathVariable int id, @RequestBody UpdateReqDto dto) {
		System.out.println("update()");
		userRepository.update(id, dto);
		return new CommonDto<>(HttpStatus.OK.value());
	}
}
