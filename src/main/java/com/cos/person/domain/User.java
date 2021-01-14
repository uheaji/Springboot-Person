package com.cos.person.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// IoC 컨테이너에서 관리하면 안됨-> findAll()은 여러개 만들어줘야하기때문이다.
public class User {
	private int id;
	private String username;
	private String password;
	private String phone;
}
