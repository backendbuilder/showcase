package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

import java.math.BigDecimal;

public class BalanceIsSufficientVerification extends Verification {

    private final BigDecimal balance;
    private final BigDecimal transactionAmount;

    public BalanceIsSufficientVerification(BigDecimal balance, BigDecimal transactionAmount) {
        this.balance = balance;
        this.transactionAmount = transactionAmount;
    }

    @Override
    VerificationStatus verify() {
       return (balance.compareTo(transactionAmount)>= 0) ? VerificationStatus.VERIFIED_SUCCESFULLY : VerificationStatus.BALANCE_INSUFFICIENT;
    }



/*    @Override
    public VerificationStatus verify(PendingTransactionDto dto, BankAccount bankAccount) {
        boolean balanceIsSufficient = bankAccount.getBalance().compareTo(dto.amount()) >= 0;
        if (balanceIsSufficient) {
            return VerificationStatus.VERIFIED_SUCCESFULLY;
        } else {
            return VerificationStatus.BALANCE_INSUFFICIENT;

        }
    }*/
}
