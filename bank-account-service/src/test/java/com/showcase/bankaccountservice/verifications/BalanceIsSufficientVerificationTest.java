package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceIsSufficientVerificationTest {

    @Test
    public void testVerifyWithSufficientBalance() {
        // Prepare
        BigDecimal balance = BigDecimal.valueOf(1000);
        BigDecimal transactionAmount = BigDecimal.valueOf(500);
        BalanceIsSufficientVerification verification = new BalanceIsSufficientVerification(balance, transactionAmount);

        // Perform
        VerificationStatus result = verification.verify();

        // Assert
        assertEquals(VerificationStatus.VERIFIED_SUCCESFULLY, result);
    }

    @Test
    public void testVerifyWithInsufficientBalance() {
        // Prepare
        BigDecimal balance = BigDecimal.valueOf(100);
        BigDecimal transactionAmount = BigDecimal.valueOf(500);
        BalanceIsSufficientVerification verification = new BalanceIsSufficientVerification(balance, transactionAmount);

        // Perform
        VerificationStatus result = verification.verify();

        // Assert
        assertEquals(VerificationStatus.BALANCE_INSUFFICIENT, result);
    }

}