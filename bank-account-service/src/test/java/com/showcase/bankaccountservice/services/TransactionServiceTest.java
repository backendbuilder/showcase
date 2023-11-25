package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.model.dtos.TransactionExecutionHelper;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.repositories.BankAccountRepository;
import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import com.showcase.sharedlibrary.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    private TransactionService transactionService;

    private static PendingTransactionDto pendingTransactionDto;
    private static BankAccount senderBankAccount;
    private static BankAccount receiverBankAccount;
    private final static String ACCOUNTHOLDER1 = "HarryBanks";
    private final static String ACCOUNTHOLDER2 = "BerryDimes";
    private final static String SENDER_ACCOUNT_ID = "IBAN1111";
    private final static String RECEIVER_ACCOUNT_ID = "IBAN2222";
    private final static String TRANSACTION_ID = "TransactionId-1";


    @BeforeAll
    static void setup() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal amount2 = BigDecimal.valueOf(2000);
        BigDecimal transactionAmount = BigDecimal.valueOf(200);
        pendingTransactionDto = new PendingTransactionDto(TRANSACTION_ID, SENDER_ACCOUNT_ID, RECEIVER_ACCOUNT_ID, transactionAmount, ACCOUNTHOLDER1);
        senderBankAccount = new BankAccount();
        senderBankAccount.setId(SENDER_ACCOUNT_ID);
        senderBankAccount.setAccountHolder(ACCOUNTHOLDER1);
        senderBankAccount.setBalance(amount);
        receiverBankAccount = new BankAccount();
        receiverBankAccount.setId(RECEIVER_ACCOUNT_ID);
        receiverBankAccount.setAccountHolder(ACCOUNTHOLDER2);
        receiverBankAccount.setBalance(amount2);
    }

    @Test
    void startTransactionProcedureShouldReturnTransactionStatusProcessed() {
        // Prepare
        when(bankAccountRepository.findById(SENDER_ACCOUNT_ID)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(RECEIVER_ACCOUNT_ID)).thenReturn(Optional.of(receiverBankAccount));
        when(verificationService.processVerifications(any(TransactionExecutionHelper.class))).thenReturn(VerificationStatus.VERIFIED_SUCCESSFULLY);

        // Perform
        ProcessedTransactionDto result = transactionService.startTransactionProcedure(pendingTransactionDto);

        // Assert
        assertEquals(TransactionStatus.PROCESSED, result.transactionStatus());
        verify(bankAccountRepository).saveAll(List.of(senderBankAccount, receiverBankAccount));
    }

    @Test
    void startTransactionProcedure_SenderAccountNotFound_ShouldReturnTransactionStatusFailed() {
        // Prepare
        when(bankAccountRepository.findById(SENDER_ACCOUNT_ID)).thenReturn(Optional.empty());
        when(bankAccountRepository.findById(RECEIVER_ACCOUNT_ID)).thenReturn(Optional.of(receiverBankAccount));

        // Perform
        ProcessedTransactionDto result = transactionService.startTransactionProcedure(pendingTransactionDto);

        // Assert
        assertEquals(TransactionStatus.FAILED, result.transactionStatus());
    }

    @Test
    void startTransactionProcedure_ReceiverAccountNotFound_ShouldReturnTransactionStatusFailed() {
        // Prepare
        when(bankAccountRepository.findById(SENDER_ACCOUNT_ID)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(RECEIVER_ACCOUNT_ID)).thenReturn(Optional.empty());

        // Perform
        ProcessedTransactionDto result = transactionService.startTransactionProcedure(pendingTransactionDto);

        // Assert
        assertEquals(TransactionStatus.FAILED, result.transactionStatus());
    }

    @Test
    void startTransactionProcedure_WithInsufficientBalance_ShouldReturnTransactionStatusDeclined() {
        // Prepare
        when(bankAccountRepository.findById(SENDER_ACCOUNT_ID)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(RECEIVER_ACCOUNT_ID)).thenReturn(Optional.of(receiverBankAccount));
        when(verificationService.processVerifications(any(TransactionExecutionHelper.class))).thenReturn(VerificationStatus.BALANCE_INSUFFICIENT);

        // Perform
        ProcessedTransactionDto result = transactionService.startTransactionProcedure(pendingTransactionDto);

        // Assert
        assertEquals(TransactionStatus.DECLINED, result.transactionStatus());
    }

    @Test
    void startTransactionProcedure_WithSenderIsNotTheAccountHolder_ShouldReturnTransactionStatusDeclined() {
        // Prepare
        when(bankAccountRepository.findById(SENDER_ACCOUNT_ID)).thenReturn(Optional.of(senderBankAccount));
        when(bankAccountRepository.findById(RECEIVER_ACCOUNT_ID)).thenReturn(Optional.of(receiverBankAccount));
        when(verificationService.processVerifications(any(TransactionExecutionHelper.class))).thenReturn(VerificationStatus.SENDER_IS_NOT_ACCOUNTHOLDER);

        // Perform
        ProcessedTransactionDto result = transactionService.startTransactionProcedure(pendingTransactionDto);

        // Assert
        assertEquals(TransactionStatus.DECLINED, result.transactionStatus());
    }
}