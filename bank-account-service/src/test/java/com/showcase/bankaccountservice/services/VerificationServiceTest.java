package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.model.dtos.TransactionExecutionHelper;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VerificationServiceTest {

    private final VerificationService verificationService = new VerificationService();

    private static BankAccount senderBankAccount;
    private static BankAccount receiverBankAccount;
    private final static String ACCOUNTHOLDER1 = "HarryBanks";
    private final static String ACCOUNTHOLDER2 = "BerryDimes";
    private final static String SENDER_ACCOUNT_ID = "IBAN1111";
    private final static String RECEIVER_ACCOUNT_ID = "IBAN2222";
    private final static String TRANSACTION_ID = "TransactionId-1";
    private static BigDecimal transactionAmount;

    @BeforeAll
    static void setup() {
        BigDecimal balance2 = BigDecimal.valueOf(2000);
        senderBankAccount = new BankAccount();
        senderBankAccount.setId(SENDER_ACCOUNT_ID);
        senderBankAccount.setAccountHolder(ACCOUNTHOLDER1);
        receiverBankAccount = new BankAccount();
        receiverBankAccount.setId(RECEIVER_ACCOUNT_ID);
        receiverBankAccount.setAccountHolder(ACCOUNTHOLDER2);
        receiverBankAccount.setBalance(balance2);
    }

    @Test
    public void processVerificationsShouldReturnVerifiedSuccessfully() {
        // Prepare
        BigDecimal balance1 = BigDecimal.valueOf(1000);
        senderBankAccount.setBalance(balance1);
        transactionAmount = BigDecimal.valueOf(200);
        TransactionExecutionHelper helper = new TransactionExecutionHelper(senderBankAccount, receiverBankAccount, transactionAmount, ACCOUNTHOLDER1, TRANSACTION_ID);

        // Perform
        VerificationStatus result = verificationService.processVerifications(helper);

        // Assert
        assertEquals(VerificationStatus.VERIFIED_SUCCESFULLY, result);
    }

    @Test
    public void processVerificationsShouldReturnBalanceInsufficient() {
        // Prepare
        BigDecimal balance1 = BigDecimal.valueOf(1000);
        senderBankAccount.setBalance(balance1);
        transactionAmount = BigDecimal.valueOf(2000);
        TransactionExecutionHelper helper = new TransactionExecutionHelper(senderBankAccount, receiverBankAccount, transactionAmount, ACCOUNTHOLDER1, TRANSACTION_ID);

        // Perform
        VerificationStatus result = verificationService.processVerifications(helper);

        // Assert
        assertEquals(VerificationStatus.BALANCE_INSUFFICIENT, result);
    }

    @Test
    public void processVerificationsShouldReturnSenderIsNotAccounholder() {
        // Prepare
        BigDecimal balance1 = BigDecimal.valueOf(1000);
        senderBankAccount.setBalance(balance1);
        transactionAmount = BigDecimal.valueOf(200);
        String wrongAccountHolder = "wrong AccountHolder";
        TransactionExecutionHelper helper = new TransactionExecutionHelper(senderBankAccount, receiverBankAccount, transactionAmount, wrongAccountHolder, TRANSACTION_ID);

        // Perform
        VerificationStatus result = verificationService.processVerifications(helper);

        // Assert
        assertEquals(VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER, result);
    }
}