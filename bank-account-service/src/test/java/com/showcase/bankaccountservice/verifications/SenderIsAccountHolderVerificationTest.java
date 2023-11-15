package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SenderIsAccountHolderVerificationTest {
    @Test
    public void testVerifyWithMatchingAccountHolder() {
        // Arrange
        String accountHolder = "HansDimes";
        String verifiedUser = "HansDimes";
        SenderIsAccountHolderVerification verification = new SenderIsAccountHolderVerification(accountHolder, verifiedUser);

        // Act
        VerificationStatus result = verification.verify();

        // Assert
        assertEquals(VerificationStatus.VERIFIED_SUCCESFULLY, result);
    }

    @Test
    public void testVerifyWithMismatchedAccountHolder() {
        // Arrange
        String accountHolder = "HansDimes";
        String verifiedUser = "DifferentUser";
        SenderIsAccountHolderVerification verification = new SenderIsAccountHolderVerification(accountHolder, verifiedUser);

        // Act
        VerificationStatus result = verification.verify();

        // Assert
        assertEquals(VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER, result);
    }

}