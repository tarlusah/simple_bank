package com.vinra.simple_banking.repository;

import com.vinra.simple_banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
