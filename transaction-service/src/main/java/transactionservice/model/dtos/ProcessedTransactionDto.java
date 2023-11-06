package transactionservice.model.dtos;

import java.math.BigDecimal;

public record ProcessedTransactionDto(String sender, String recipient, BigDecimal amount){

}
