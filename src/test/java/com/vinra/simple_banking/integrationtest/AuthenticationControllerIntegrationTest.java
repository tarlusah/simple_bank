package com.vinra.simple_banking.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinra.simple_banking.constant.Role;
import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.controller.dto.LoginRequest;
import com.vinra.simple_banking.controller.dto.RegisterRequest;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.repository.UserRepository;
import com.vinra.simple_banking.utils.JwtUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	@BeforeEach
	void setup(){
		userRepository.deleteAll();
	}



	@Test
	void testRegisterUser() throws Exception {
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("test@example.com");
		registerRequest.setPassword("password123");
		registerRequest.setFirstName("Test");
		registerRequest.setLastName("User");

		mockMvc.perform(
			post("/api/v1/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerRequest))
		).andExpectAll(
			status().isOk(),
			jsonPath("$.message").value("Successfully registered"),
			jsonPath("$.user.email").value("test@example.com")
		);

	}

	@Test
	void testLoginUser() throws Exception {
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword(passwordEncoder.encode("password123"));
		user.setRole(Role.USER);
		userRepository.save(user);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("test@example.com");
		loginRequest.setPassword("password123");

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("Login Success"))
			.andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	void testLoginUser_InvalidCredentials() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("wrong@example.com");
		loginRequest.setPassword("wrongpassword");

		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andExpect(status().isUnauthorized());
	}

}
