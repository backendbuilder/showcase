package com.showcase.bankaccountservice.kafka;

import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Producer {

    private static final String TOPIC = "processed_transactions_topic";
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ProcessedTransactionDto processedTransactionDto){
        Message<ProcessedTransactionDto> kafkaMessage = MessageBuilder
                .withPayload(processedTransactionDto)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
        System.out.println("BankAccountService producer::sendMessage()");
        kafkaTemplate.send(kafkaMessage);
    }
}
