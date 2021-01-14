package com.cos.person.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommonDto<T> {
	private int ststusCode;
	private T data;
	
	
	public CommonDto(int ststusCode, T data) {
		super();
		this.ststusCode = ststusCode;
		this.data = data;
	}


	public CommonDto(int ststusCode) {
		super();
		this.ststusCode = ststusCode;
	}
	
	
}
