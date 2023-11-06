package com.showcase.bankaccountservice.kafka;

import java.math.BigDecimal;

public record PendingTransactionDto(String id, String sender, String recipient, BigDecimal amount){

}
