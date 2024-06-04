package com.vinra.simple_banking.service;

import com.vinra.simple_banking.repository.TransactionRepository;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.Transaction;
import com.vinra.simple_banking.constant.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

	private final TransactionRepository repository;

	@Override
	public Transaction initTransaction(Account account) {
		return createTransaction(account, BigDecimal.valueOf(500_000), TransactionType.DEPOSIT);
	}

	@Override
	public Transaction createTransaction(Account account, BigDecimal amount, TransactionType type) {
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setType(type);
		transaction.setDate(LocalDateTime.now());

		transaction.setAccount(account);
		updateBalance(account, amount, type);
		repository.save(transaction);

		return transaction;
	}

	private void updateBalance(Account account, BigDecimal amount, TransactionType type) {
		if (TransactionType.DEPOSIT.equals(type)) {
			account.setBalance(account.getBalance().add(amount));
		}else {
			account.setBalance(account.getBalance().subtract(amount));
		}
	}


}
