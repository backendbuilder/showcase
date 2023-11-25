package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.model.dtos.TransactionExecutionHelper;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.repositories.BankAccountRepository;
import com.showcase.bankaccountservice.verifications.enums.VerificationStatus;
import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import com.showcase.sharedlibrary.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private VerificationService verificationService;
    private BankAccountRepository bankAccountRepository;

    /**
     * Perform verifications and execute transaction is verified succesfully
     * @param pendingTransactionDto
     * @return
     */
    public ProcessedTransactionDto startTransactionProcedure(PendingTransactionDto pendingTransactionDto){
        VerificationStatus verificationStatus;
        TransactionStatus transactionStatus;

        Optional<BankAccount> sender = bankAccountRepository.findById(pendingTransactionDto.sender());
        Optional<BankAccount> recipient = bankAccountRepository.findById(pendingTransactionDto.recipient());

        if(sender.isPresent() && recipient.isPresent()){
            TransactionExecutionHelper transactionExecutionHelper = dtoToTransactionHelper(pendingTransactionDto, sender.get(), recipient.get());
            verificationStatus = verificationService.processVerifications(transactionExecutionHelper);
            if (verificationStatus == VerificationStatus.VERIFIED_SUCCESSFULLY){
                transactionStatus = executeTransaction(transactionExecutionHelper);
            } else transactionStatus = TransactionStatus.DECLINED;
        } else transactionStatus = TransactionStatus.FAILED;

        return pendingTransactionToProcessedTransactionDto(pendingTransactionDto, transactionStatus);
    }

    /**
     * Updates balance of verifiedUser and receiver
     * @param transactionExecutionHelper
     * @return
     */
    public TransactionStatus executeTransaction(TransactionExecutionHelper transactionExecutionHelper) {
        BigDecimal newBalanceSender = transactionExecutionHelper.sendingAccount().getBalance().subtract(transactionExecutionHelper.amount());
        transactionExecutionHelper.sendingAccount().setBalance(newBalanceSender);
        BigDecimal newBalanceRecipient = transactionExecutionHelper.receivingAccount().getBalance().add(transactionExecutionHelper.amount());
        transactionExecutionHelper.receivingAccount().setBalance(newBalanceRecipient);
        try {
            bankAccountRepository.saveAll(List.of(transactionExecutionHelper.sendingAccount(), transactionExecutionHelper.receivingAccount()));
            return TransactionStatus.PROCESSED;
        } catch (Exception e){
            return TransactionStatus.FAILED;
        }
    }

    private TransactionExecutionHelper dtoToTransactionHelper(PendingTransactionDto pendingTransactionDto, BankAccount sender, BankAccount recipient){
        return new TransactionExecutionHelper(sender, recipient, pendingTransactionDto.amount(), pendingTransactionDto.accountHolder(), pendingTransactionDto.id());
    }
    private ProcessedTransactionDto pendingTransactionToProcessedTransactionDto(PendingTransactionDto dto, TransactionStatus transactionStatus){
        return new ProcessedTransactionDto(dto.id(), dto.sender(), dto.recipient(),dto.amount(), transactionStatus);
    }
}
