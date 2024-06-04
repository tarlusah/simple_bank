package com.vinra.simple_banking.controller.converter;

import com.vinra.simple_banking.controller.dto.RegisterRequest;
import com.vinra.simple_banking.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationDtoConverter {

	public User convertUser(RegisterRequest request){
		return User.builder()
			.firstName(request.getFirstName())
			.lastName(request.getLastName())
			.email(request.getEmail())
			.password(request.getPassword())
			.build();
	}
}
