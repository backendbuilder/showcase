package com.showcase.sharedlibrary.dtos;

import java.math.BigDecimal;

public record ProcessedTransactionDto(String sender, String recipient, BigDecimal amount){

}
