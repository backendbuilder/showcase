package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceIsSufficientVerificationTest {

    @Test
    public void balanceIsSufficientVerification_WithSufficientBalance_AndExpectVerifiedSuccessfully() {
        // Prepare
        BigDecimal balance = BigDecimal.valueOf(1000);
        BigDecimal transactionAmount = BigDecimal.valueOf(500);
        BalanceIsSufficientVerification verification = new BalanceIsSufficientVerification(balance, transactionAmount);

        // Perform
        VerificationStatus result = verification.verify();

        // Assert
        assertEquals(VerificationStatus.VERIFIED_SUCCESSFULLY, result);
    }

    @Test
    public void balanceIsSufficientVerification_WithInSufficientBalance_AndExpectBalanceInsufficient() {
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