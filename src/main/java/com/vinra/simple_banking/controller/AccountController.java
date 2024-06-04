package com.vinra.simple_banking.controller;

import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.service.UserService;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.controller.dto.TransactionRequest;
import com.vinra.simple_banking.controller.dto.TransactionResponse;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.service.AccountService;
import com.vinra.simple_banking.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

	private final UserService userService;
	private final AccountService accountService;
	private final TransactionService transactionService;

	@GetMapping(path = "/balance")
	public ResponseEntity<Account> balance(HttpServletRequest request) {
		User user = (User) request.getAttribute("userDetails");
		Account account = userService.getAccountByUserId(user.getId());
		return ResponseEntity.ok(account);
	}

	@PostMapping(path = "/transaction")
	public ResponseEntity<TransactionResponse> transaction(HttpServletRequest request, @Valid @RequestBody TransactionRequest transactionRequest) {

		var response = new TransactionResponse();
		try {
			User user = (User) request.getAttribute("userDetails");
			Account account = userService.getAccountByUserId(user.getId());
			accountService.checkAccountBalance(account, TransactionType.valueOf(transactionRequest.getTransactionType()), transactionRequest.getAmount());
			transactionService.createTransaction(account, transactionRequest.getAmount(), TransactionType.valueOf(transactionRequest.getTransactionType()));
			response = TransactionResponse.builder()
				.message("Transaction Successful")
				.build();
		}catch (IllegalArgumentException ignored) {
			response = TransactionResponse.builder()
				.message(ignored.getMessage())
				.build();
		}
		return ResponseEntity.ok(response);
	}
}
