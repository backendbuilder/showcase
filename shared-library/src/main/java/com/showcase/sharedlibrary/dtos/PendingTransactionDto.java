package com.showcase.sharedlibrary.dtos;

import com.showcase.sharedlibrary.enums.TransactionStatus;

import java.math.BigDecimal;

public record PendingTransactionDto(String id, String sender, String recipient, BigDecimal amount, String accountHolder){

}
