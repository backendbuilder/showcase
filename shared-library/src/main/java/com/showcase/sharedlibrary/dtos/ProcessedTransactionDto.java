package com.showcase.sharedlibrary.dtos;

import com.showcase.sharedlibrary.enums.TransactionStatus;

import java.math.BigDecimal;

public record ProcessedTransactionDto(String id, String sender, String recipient, BigDecimal amount, TransactionStatus transactionStatus){

}
