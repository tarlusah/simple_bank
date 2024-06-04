package com.vinra.simple_banking.service;

import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.model.Account;

public interface UserService {

	User getUserById(Long id);
	User registerUser(User user);
	User getUserByEmail(String email);
	Account getAccountByUserId(Long userId);

}
