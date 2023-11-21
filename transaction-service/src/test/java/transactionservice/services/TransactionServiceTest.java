package transactionservice.services;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import transactionservice.kafka.Producer;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.mappers.TransactionMapper;
import transactionservice.model.entities.Transaction;
import transactionservice.repositories.TransactionRepository;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Producer producer;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testInitiateTransaction() {
        // Prepare
        String sender = "IBAN1111";
        String recipient = "IBAN2222";
        BigDecimal amount = BigDecimal.valueOf(200);
        String accountHolder = "HarryBanks";
        String transactionId = "ID1111";
        TransactionRequestDto requestDto = new TransactionRequestDto(sender, recipient, amount, accountHolder);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setAmount(amount);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(transactionId);
        savedTransaction.setSender(sender);
        savedTransaction.setRecipient(recipient);
        savedTransaction.setAmount(amount);

        PendingTransactionDto pendingTransactionDto = new PendingTransactionDto(transactionId, sender, recipient, amount, accountHolder);

        when(mapper.transactionRequestDtoToTransaction(requestDto)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(mapper.transactionToPendingTransactionDto(savedTransaction)).thenReturn(pendingTransactionDto);

        // Perform
        transactionService.initiateTransaction(requestDto);

        // Assert
        verify(mapper).transactionRequestDtoToTransaction(requestDto);
        verify(transactionRepository).save(transaction);
        verify(mapper).transactionToPendingTransactionDto(savedTransaction);
        verify(producer).sendMessage(pendingTransactionDto);
    }
}