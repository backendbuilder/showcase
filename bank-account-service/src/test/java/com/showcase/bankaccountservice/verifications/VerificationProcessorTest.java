package com.showcase.bankaccountservice.verifications;

import com.showcase.bankaccountservice.model.dtos.TransactionExecutionHelper;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class VerificationProcessorTest {

    private VerificationProcessor verificationProcessor = new VerificationProcessor();

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
        TransactionExecutionHelper transactionExecutionHelper = new TransactionExecutionHelper(senderBankAccount, receiverBankAccount, transactionAmount, ACCOUNTHOLDER1, TRANSACTION_ID);
        verificationProcessor.addVerification(new SenderIsAccountHolderVerification(transactionExecutionHelper.sendingAccount().getAccountHolder(), transactionExecutionHelper.verifiedUser()));
        verificationProcessor.addVerification(new BalanceIsSufficientVerification(transactionExecutionHelper.sendingAccount().getBalance(), transactionExecutionHelper.amount()));

        // Perform
        VerificationStatus result = verificationProcessor.verify();

        // Assert
        assertEquals(VerificationStatus.VERIFIED_SUCCESFULLY, result);
    }

    @Test
    public void processVerificationsShouldReturnBalanceInsufficient() {
        // Prepare
        BigDecimal balance1 = BigDecimal.valueOf(1000);
        senderBankAccount.setBalance(balance1);
        transactionAmount = BigDecimal.valueOf(2000);
        TransactionExecutionHelper transactionExecutionHelper = new TransactionExecutionHelper(senderBankAccount, receiverBankAccount, transactionAmount, ACCOUNTHOLDER1, TRANSACTION_ID);
        verificationProcessor.addVerification(new SenderIsAccountHolderVerification(transactionExecutionHelper.sendingAccount().getAccountHolder(), transactionExecutionHelper.verifiedUser()));
        verificationProcessor.addVerification(new BalanceIsSufficientVerification(transactionExecutionHelper.sendingAccount().getBalance(), transactionExecutionHelper.amount()));

        // Perform
        VerificationStatus result = verificationProcessor.verify();

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
        TransactionExecutionHelper transactionExecutionHelper = new TransactionExecutionHelper(senderBankAccount, receiverBankAccount, transactionAmount, wrongAccountHolder, TRANSACTION_ID);
        verificationProcessor.addVerification(new SenderIsAccountHolderVerification(transactionExecutionHelper.sendingAccount().getAccountHolder(), transactionExecutionHelper.verifiedUser()));
        verificationProcessor.addVerification(new BalanceIsSufficientVerification(transactionExecutionHelper.sendingAccount().getBalance(), transactionExecutionHelper.amount()));

        // Perform
        VerificationStatus result = verificationProcessor.verify();

        // Assert
        assertEquals(VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER, result);
    }

}