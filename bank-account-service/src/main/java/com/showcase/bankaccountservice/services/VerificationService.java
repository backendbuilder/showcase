package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.Exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.kafka.PendingTransactionDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class VerificationService {

    private BankAccountService bankAccountService;

    public boolean SenderIsAccountHolder(PendingTransactionDto pendingTransactionDto) {
        try {
            BankAccountResponseDto bankAccount = bankAccountService.readBankAccountById(pendingTransactionDto.sender());
            if (pendingTransactionDto.sender().equals(bankAccount.accountHolder())) {
                return true;
            }
        } catch (EntityNotFoundException e) {
            return false;
        }
        return false;
    }
    public boolean balanceIsSufficient(PendingTransactionDto pendingTransactionDto) {
        try {
            BankAccountResponseDto bankAccount = bankAccountService.readBankAccountById(pendingTransactionDto.sender());
            if (bankAccount.balance().compareTo(pendingTransactionDto.amount()) < 0) {
                return false;
            }
        } catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }
}
