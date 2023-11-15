package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;

import java.math.BigDecimal;

public class BalanceIsSufficientVerification implements Verification {

    private final BigDecimal balance;
    private final BigDecimal transactionAmount;

    public BalanceIsSufficientVerification(BigDecimal balance, BigDecimal transactionAmount) {
        this.balance = balance;
        this.transactionAmount = transactionAmount;
    }

    @Override
    public VerificationStatus verify() {
       return (balance.compareTo(transactionAmount)>= 0) ? VerificationStatus.VERIFIED_SUCCESFULLY : VerificationStatus.BALANCE_INSUFFICIENT;
    }
}
