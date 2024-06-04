package com.vinra.simple_banking.service;

import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.Transaction;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	@Test
	void givenValidUser_whenCreateAccount_thenReturnAccount() {
		User user = new User();
		user.setFirstName("vinra");
		user.setLastName("pandia");
		user.setEmail("vinra.pandia@gmail.com");
		Account account = new Account();
		Transaction transaction = new Transaction();

		when(accountRepository.save(any(Account.class))).thenReturn(account);
		when(transactionService.initTransaction(any(Account.class))).thenReturn(transaction);

		Account createdAccount = accountServiceImpl.createAccount(user);

		assertNotNull(createdAccount);
		assertEquals(BigDecimal.ZERO, createdAccount.getBalance());
		assertEquals(user, createdAccount.getUser());
		assertEquals(1, createdAccount.getTransactions().size());

		verify(accountRepository, times(1)).save(any(Account.class));
		verify(transactionService, times(1)).initTransaction(any(Account.class));
	}

	@Test
	void testAccountBalance(){
		Account account = new Account();
		account.setBalance(BigDecimal.valueOf(100));

		//test valid deposit
		assertDoesNotThrow(() -> accountServiceImpl.checkAccountBalance(account, TransactionType.DEPOSIT, BigDecimal.valueOf(100)));

		//test valid withdraw
		assertDoesNotThrow(() -> accountServiceImpl.checkAccountBalance(account, TransactionType.WITHDRAW, BigDecimal.valueOf(100)));

		//test invalid withdraw
		Exception exception = assertThrows(IllegalArgumentException.class, () ->
			accountServiceImpl.checkAccountBalance(account, TransactionType.WITHDRAW, BigDecimal.valueOf(2000)));
		assertEquals("Account balance must be greater than withdraw amount", exception.getMessage());

		// Test negative amount when deposit
			exception = assertThrows(IllegalArgumentException.class, () ->
				accountServiceImpl.checkAccountBalance(account, TransactionType.DEPOSIT, BigDecimal.valueOf(-100))
		);
		assertEquals("Amount must be greater than zero", exception.getMessage());

		// Test negative amount when withdraw
		exception = assertThrows(IllegalArgumentException.class, () ->
			accountServiceImpl.checkAccountBalance(account, TransactionType.WITHDRAW, BigDecimal.valueOf(-100))
		);
		assertEquals("Amount must be greater than zero", exception.getMessage());

	}
}