package com.showcase.bankaccountservice.model.dtos;

import com.showcase.bankaccountservice.model.entities.BankAccount;

import java.math.BigDecimal;

public record TransactionExecutionHelper (
        BankAccount sendingAccount,
        BankAccount receivingAccount,
        BigDecimal amount,
        String verifiedUser,
        String transactionId
){
}
