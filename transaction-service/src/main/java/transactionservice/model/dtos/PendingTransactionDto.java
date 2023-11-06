package transactionservice.model.dtos;

import java.math.BigDecimal;

public record PendingTransactionDto(String id, String sender, String recipient, BigDecimal amount){

}
