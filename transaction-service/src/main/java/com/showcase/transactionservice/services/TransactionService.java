package com.showcase.transactionservice.services;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.enums.TransactionStatus;
import com.showcase.transactionservice.kafka.Producer;
import com.showcase.transactionservice.model.dtos.TransactionRequestDto;
import com.showcase.transactionservice.model.dtos.TransactionResponseDto;
import com.showcase.transactionservice.model.dtos.mappers.TransactionMapper;
import com.showcase.transactionservice.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.showcase.transactionservice.model.entities.Transaction;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final Producer producer;
    private final TransactionMapper mapper;

    public TransactionResponseDto initiateTransaction(TransactionRequestDto transactionRequestDto){
        Transaction transaction = mapper.transactionRequestDtoToTransaction(transactionRequestDto);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction = transactionRepository.save(transaction);
        PendingTransactionDto pendingTransactionDto = new PendingTransactionDto(
                transaction.getId(),
                transactionRequestDto.sender(),
                transactionRequestDto.recipient(),
                transactionRequestDto.amount(),
                transactionRequestDto.accountHolder());
        producer.sendMessage(pendingTransactionDto);
        return mapper.pendingTransactionDtoToToTransactionResponseDto(pendingTransactionDto);
    }

}
