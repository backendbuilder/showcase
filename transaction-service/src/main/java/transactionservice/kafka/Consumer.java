package transactionservice.kafka;

import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import transactionservice.model.dtos.mappers.TransactionMapper;
import transactionservice.repositories.TransactionRepository;


@Service
@AllArgsConstructor
public class Consumer {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

    @KafkaListener(topics = "processed_transactions_topic",groupId = "consumer_group_processed_transactions")
    public void consumeMessage( ConsumerRecord<String, ProcessedTransactionDto> record
    ){
        System.out.println("Transaction-service - Consumer::consumeMessage");
        transactionRepository.save(mapper.processedTransactionDtoToTransaction(record.value()));
    }
}
