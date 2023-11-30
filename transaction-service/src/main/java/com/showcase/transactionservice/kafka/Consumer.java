package com.showcase.transactionservice.kafka;

import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import com.showcase.transactionservice.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.showcase.transactionservice.model.dtos.mappers.TransactionMapper;


@Service
@AllArgsConstructor
public class Consumer {

    private static final String PROCESSED_TRANSACTIONS_TOPIC = "processed_transactions_topic";
    private static final String CONSUMER_GROUP_PROCESSED_TRANSACTIONS = "consumer_group_processed_transactions";
    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;

    @KafkaListener(topics = PROCESSED_TRANSACTIONS_TOPIC, groupId = CONSUMER_GROUP_PROCESSED_TRANSACTIONS)
    public void consumeMessage( ConsumerRecord<String, ProcessedTransactionDto> record
    ){
        //TODO remove println() or replace with logger
        System.out.println("Transaction-service - Consumer::consumeMessage");
        transactionRepository.save(mapper.processedTransactionDtoToTransaction(record.value()));
    }
}
