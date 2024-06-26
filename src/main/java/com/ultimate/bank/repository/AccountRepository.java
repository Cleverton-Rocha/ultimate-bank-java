package com.ultimate.bank.repository;

import com.ultimate.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUserCPF(String CPF);
}
