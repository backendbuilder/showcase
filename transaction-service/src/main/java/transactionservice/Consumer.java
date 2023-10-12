package transactionservice;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class Consumer {

    @KafkaListener(topics = "pending_transactions_topic",groupId = "consumer_group_processed_transactions")
    public void consumeMessage( ConsumerRecord<String, String> record
    ){
        System.out.println("topic = " + record.topic());
        System.out.println("partition = " + record.partition());
        System.out.println("message = " + record.value());
    }
}