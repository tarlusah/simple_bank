package com.vinra.simple_banking.service;

import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.repository.AccountRepository;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

	private final AccountRepository repository;
	private final TransactionService transactionService;

	@Override
	public Account createAccount(User user) {
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(0));
		account.setUser(user);
		repository.save(account);

		Transaction transaction = transactionService.initTransaction(account);
		account.getTransactions().add(transaction);

		return account;
	}

	@Override
	public void checkAccountBalance(Account account, TransactionType type, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Amount must be greater than zero");
		}else if (type == TransactionType.WITHDRAW && account.getBalance().compareTo(amount) < 0) {
			throw new IllegalArgumentException("Account balance must be greater than withdraw amount");
		}
	}


}
