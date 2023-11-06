package transactionservice.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import transactionservice.model.enums.TransactionStatus;

import java.math.BigDecimal;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
        String id;
        String sender;
        String recipient;
        BigDecimal amount;
    @Enumerated(EnumType.ORDINAL)
        TransactionStatus status;


    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}

