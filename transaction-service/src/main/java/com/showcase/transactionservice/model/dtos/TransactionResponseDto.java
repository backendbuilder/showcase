package com.showcase.transactionservice.model.dtos;

import java.math.BigDecimal;

public record TransactionResponseDto(String id, String sender, String recipient, BigDecimal amount, String accountHolder){

}
