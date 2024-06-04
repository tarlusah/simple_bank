package com.vinra.simple_banking.service;

import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.Transaction;
import com.vinra.simple_banking.constant.TransactionType;

import java.math.BigDecimal;

public interface TransactionService {

	Transaction initTransaction(Account account);

	Transaction createTransaction(Account account, BigDecimal amount, TransactionType type);
}
