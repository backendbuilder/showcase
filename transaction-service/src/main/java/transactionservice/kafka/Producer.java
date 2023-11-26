package transactionservice.kafka;

import com.showcase.sharedlibrary.dtos.PendingTransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class Producer {

    private static final String PENDING_TRANSACTIONS_TOPIC = "pending_transactions_topic";
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(PendingTransactionDto pendingTransactionDto){
        Message<PendingTransactionDto> kafkaMessage = MessageBuilder
                .withPayload(pendingTransactionDto)
                .setHeader(KafkaHeaders.TOPIC, PENDING_TRANSACTIONS_TOPIC)
                .build();
        //TODO remove println() or replace with logger
        System.out.println("Transaction-service - Producer::sendMessage()");
        kafkaTemplate.send(kafkaMessage);
    }

}
