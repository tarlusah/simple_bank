package com.vinra.simple_banking.service;

import com.vinra.simple_banking.constant.Role;
import com.vinra.simple_banking.model.Account;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	UserRepository userRepository;

	@Mock
	AccountService accountService;

	@InjectMocks
	UserServiceImpl userService;

	@Test
	void testGetUserById_UserExists() {
		User user = new User();
		user.setId(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		User foundUser = userService.getUserById(1L);

		assertNotNull(foundUser);
		assertEquals(1L, foundUser.getId());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void testGetUserById_UserNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(1L));

		assertEquals("User not Found 1", exception.getMessage());
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void testRegisterUser() {
		User user = new User();
		when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
		when(accountService.createAccount(any(User.class))).thenReturn(new Account());

		User registeredUser = userService.registerUser(user);

		assertNotNull(registeredUser);
		assertEquals(Role.USER, registeredUser.getRole());
		assertNotNull(registeredUser.getAccount());
		verify(userRepository, times(1)).save(user);
		verify(accountService, times(1)).createAccount(user);
	}

	@Test
	void testGetUserByEmail_UserExists() {
		User user = new User();
		user.setEmail("test@example.com");
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

		User foundUser = userService.getUserByEmail("test@example.com");

		assertNotNull(foundUser);
		assertEquals("test@example.com", foundUser.getEmail());
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testGetUserByEmail_UserNotFound() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

		Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail("test@example.com"));

		assertEquals("Not Found ", exception.getMessage());
		verify(userRepository, times(1)).findByEmail("test@example.com");
	}

	@Test
	void testGetAccountByUserId_UserExists() {
		User user = new User();
		Account account = new Account();
		user.setAccount(account);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		Account foundAccount = userService.getAccountByUserId(1L);

		assertNotNull(foundAccount);
		assertEquals(account, foundAccount);
		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	void testGetAccountByUserId_UserNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getAccountByUserId(1L));

		assertEquals("User not Found 1", exception.getMessage());
		verify(userRepository, times(1)).findById(1L);
	}

}