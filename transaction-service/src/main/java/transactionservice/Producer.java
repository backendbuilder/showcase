package transactionservice;

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



    public void sendMessage(String message){
        Message<String> kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
        System.out.println("producer::sendMessage()");
        kafkaTemplate.send(TOPIC, message);
    }

}
