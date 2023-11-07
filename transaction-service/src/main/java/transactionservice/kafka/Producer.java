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

    private static final String TOPIC = "pending_transactions_topic";
    private KafkaTemplate<String, String> kafkaTemplate;



    public void sendMessage(PendingTransactionDto pendingTransactionDto){
        Message<PendingTransactionDto> kafkaMessage = MessageBuilder
                .withPayload(pendingTransactionDto)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
        System.out.println("producer::sendMessage()");
        kafkaTemplate.send(kafkaMessage);
    }

}
