package com.practice.efubaccount.account.repository;

import com.practice.efubaccount.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    Optional<Account> findByAccountId(Long accountId);
}
