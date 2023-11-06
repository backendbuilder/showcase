package com.showcase.bankaccountservice.repositories;

import com.showcase.bankaccountservice.model.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    List<BankAccount> findByAccountHolder(String accountHolder);
}

