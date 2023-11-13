package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.model.dtos.TransactionExecutionHelper;
import com.showcase.bankaccountservice.repositories.BankAccountRepository;
import com.showcase.bankaccountservice.verifications.*;
import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class VerificationService {

    private BankAccountRepository bankAccountRepository;

    public VerificationStatus processVerifications(TransactionExecutionHelper transactionExecutionHelper){

        VerificationProcessor verificationProcessor = new VerificationProcessor();
        verificationProcessor.addVerification(new SenderIsAccountHolderVerification(transactionExecutionHelper.sendingAccount().getAccountHolder(), transactionExecutionHelper.verifiedUser()));
        verificationProcessor.addVerification(new BalanceIsSufficientVerification(transactionExecutionHelper.sendingAccount().getBalance(), transactionExecutionHelper.amount()));
        return verificationProcessor.verify();
    }
}
