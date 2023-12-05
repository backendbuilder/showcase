package com.showcase.transactionservice.model.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequestDto(
        @NotBlank(message= "sender can't be empty")
        String sender,
        @NotBlank(message= "recipient can't be empty")
        String recipient,
        @NotNull(message= "amount can't be empty")
        @DecimalMin(value = "0.01", message = "amount is not valid, must at least be 0.01")
        BigDecimal amount,
        @NotBlank(message= "accountHolder can't be empty")
        String accountHolder){
}
