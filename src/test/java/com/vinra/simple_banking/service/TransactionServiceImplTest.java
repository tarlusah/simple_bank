package com.vinra.simple_banking.service;

import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.Transaction;
import com.vinra.simple_banking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

	@Mock
	private TransactionRepository repository;

	@InjectMocks
	private TransactionServiceImpl service;


	@Test
	void testInitTransaction() {
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(0));

		Transaction transaction = service.initTransaction(account);

		verify(repository, times(1)).save(any(Transaction.class));

		assertNotNull(transaction);
		assertEquals(BigDecimal.valueOf(500_000), transaction.getAmount());
		assertEquals(TransactionType.DEPOSIT, transaction.getType());
		assertEquals(account, transaction.getAccount());
		assertNotNull(transaction.getDate());
		assertEquals(BigDecimal.valueOf(500_000), account.getBalance());
	}

	@Test
	void testCreateWithdrawTransaction() {
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(1000));

		BigDecimal amount = BigDecimal.valueOf(500);
		TransactionType type = TransactionType.WITHDRAW;

		Transaction transaction = service.createTransaction(account, amount, type);
		verify(repository, times(1)).save(any(Transaction.class));

		assertNotNull(transaction);
		assertEquals(amount, transaction.getAmount());
		assertEquals(type, transaction.getType());
		assertEquals(account, transaction.getAccount());
		assertNotNull(transaction.getDate());
		assertEquals(BigDecimal.valueOf(500), account.getBalance());
	}

	@Test
	void testCreateDepositTransaction() {
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(1000));

		BigDecimal amount = BigDecimal.valueOf(500);
		TransactionType type = TransactionType.DEPOSIT;

		Transaction transaction = service.createTransaction(account, amount, type);
		verify(repository, times(1)).save(any(Transaction.class));

		assertNotNull(transaction);
		assertEquals(amount, transaction.getAmount());
		assertEquals(type, transaction.getType());
		assertEquals(account, transaction.getAccount());
		assertNotNull(transaction.getDate());
		assertEquals(BigDecimal.valueOf(1500), account.getBalance());
	}

	@Test
	void testUpdateBalanceDeposit(){
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(1000));
		service.createTransaction(account, BigDecimal.valueOf(500), TransactionType.DEPOSIT);

		assertEquals(BigDecimal.valueOf(1500), account.getBalance());
	}


	@Test
	void testUpdateBalanceWithdraw() {
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(1000));

		service.createTransaction(account, BigDecimal.valueOf(500), TransactionType.WITHDRAW);

		assertEquals(BigDecimal.valueOf(500), account.getBalance());
	}

}