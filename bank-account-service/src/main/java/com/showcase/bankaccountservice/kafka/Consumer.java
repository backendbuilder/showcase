package com.showcase.bankaccountservice.kafka;

import com.showcase.bankaccountservice.services.TransactionService;
import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Consumer {

    private TransactionService transactionService;
    private Producer producer;

    @KafkaListener(topics = "pending_transactions_topic",groupId = "consumer_group_pending_transactions")
    public void consumeMessage(ConsumerRecord<String, PendingTransactionDto>  record){
        System.out.println("BANK_ACCOUNT_SERVICE: consume-message" );
        PendingTransactionDto pendingTransactionDto = record.value();
        ProcessedTransactionDto processedTransactionDto = transactionService.startTransactionProcedure(pendingTransactionDto);
        producer.sendMessage(processedTransactionDto);
        System.out.println("topic = " + record.topic());
        System.out.println("partition = " + record.partition());
        System.out.println("message = " + pendingTransactionDto);
    }
}
