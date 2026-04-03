package com.practice.efubaccount.repository;

import com.practice.efubaccount.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    Optional<Account> findByAccountId(Long accountId);
}
