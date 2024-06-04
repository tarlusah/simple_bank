package com.vinra.simple_banking.service;

import com.vinra.simple_banking.repository.UserRepository;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final AccountService accountService;


	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("User not Found " + id.toString()));
	}

	@Override
	public User registerUser(User user) {
		user.setRole(Role.USER);
		userRepository.save(user);
		Account account = accountService.createAccount(user);
		user.setAccount(account);
		return user;
	}

	@Override
	public User getUserByEmail(String email) {

		User user =  userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not Found "));
		return user;

	}

	@Override
	public Account getAccountByUserId(Long userId) {

		User user = getUserById(userId);
		return user.getAccount();
	}
}
