package com.vinra.simple_banking.service;

import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.constant.TransactionType;
import com.vinra.simple_banking.model.Account;

import java.math.BigDecimal;

public interface AccountService {

	Account createAccount(User user);

	void checkAccountBalance(Account account, TransactionType type, BigDecimal amount);

}
