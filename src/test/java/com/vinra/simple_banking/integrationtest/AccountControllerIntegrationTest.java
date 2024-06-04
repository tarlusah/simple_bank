package com.vinra.simple_banking.integrationtest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinra.simple_banking.constant.Role;
import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.controller.converter.AuthenticationDtoConverter;
import com.vinra.simple_banking.controller.dto.RegisterRequest;
import com.vinra.simple_banking.controller.dto.TransactionRequest;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.repository.UserRepository;
import com.vinra.simple_banking.service.AccountService;
import com.vinra.simple_banking.service.UserService;
import com.vinra.simple_banking.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {


	@Autowired
	private MockMvc mockMvc;


	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;

	String token;

	@BeforeEach
	public void setUp() throws Exception {
		userRepository.deleteAll();
		String password ="password123";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("test@example.com");
		registerRequest.setPassword(password);
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

		Authentication authenticate = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				registerRequest.getEmail(),
				password
			)
		);
		token = jwtUtils.generateJwtToken(authenticate);
	}

	@Test
	public void testBalance() throws Exception {
		mockMvc.perform(
			get("/api/v1/account/balance")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
		).andDo(print()
		).andExpectAll(
			status().isOk(),
			jsonPath("$.balance").value(500000.00)
		);
	}

	@Test
	public void testTransaction_Successful() throws Exception {
		TransactionRequest transactionRequest = new TransactionRequest();
		transactionRequest.setTransactionType(TransactionType.DEPOSIT.name());
		transactionRequest.setAmount(BigDecimal.valueOf(500));

		mockMvc.perform(
			post("/api/v1/account/transaction")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequest))
			).andDo(print()
			)
			.andExpectAll(
				status().isOk(),
				jsonPath("$.message").value("Transaction Successful")
			);
	}

}
