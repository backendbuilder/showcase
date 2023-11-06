package transactionservice.model.dtos;

import java.math.BigDecimal;

public record TransactionRequestDto(String sender, String recipient, BigDecimal amount){

}
