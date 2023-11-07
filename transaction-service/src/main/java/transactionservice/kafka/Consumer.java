package transactionservice.kafka;

import com.showcase.sharedlibrary.dtos.ProcessedTransactionDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class Consumer {

    @KafkaListener(topics = "processed_transactions_topic",groupId = "consumer_group_processed_transactions")
    public void consumeMessage( ConsumerRecord<String, ProcessedTransactionDto> record
    ){
        System.out.println("PROCESSED TRANSACTION");
        System.out.println("topic = " + record.topic());
        System.out.println("partition = " + record.partition());
        System.out.println("message = " + record.value());
    }
}
