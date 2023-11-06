package com.showcase.bankaccountservice.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Producer {

    private static final String TOPIC = "processed_transactions_topic";
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message){
        kafkaTemplate.send(TOPIC, message);
    }

}
