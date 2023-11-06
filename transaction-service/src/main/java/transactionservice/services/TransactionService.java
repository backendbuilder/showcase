package transactionservice.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import transactionservice.kafka.Producer;
import transactionservice.model.dtos.PendingTransactionDto;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.mappers.TransactionMapper;
import transactionservice.model.entities.Transaction;
import transactionservice.repositories.TransactionRepository;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final Producer producer;
    private final TransactionMapper mapper;

    public void initiateTransaction(TransactionRequestDto transactionRequestDto){
        Transaction transaction = mapper.transactionRequestDtoToTransaction(transactionRequestDto);
        transaction = transactionRepository.save(transaction);
        PendingTransactionDto pendingTransactionDto = mapper.transactionToPendingTransactionDto(transaction);
        producer.sendMessage(pendingTransactionDto);
        //producer.send
    }

}
