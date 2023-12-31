package transactionservice.services;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import transactionservice.kafka.Producer;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.TransactionResponseDto;
import transactionservice.model.dtos.mappers.TransactionMapper;
import transactionservice.model.entities.Transaction;
import transactionservice.repositories.TransactionRepository;

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
