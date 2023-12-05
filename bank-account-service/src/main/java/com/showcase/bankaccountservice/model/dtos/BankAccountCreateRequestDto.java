package com.showcase.bankaccountservice.model.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.showcase.bankaccountservice.model.entities.BankAccount}
 */
public record BankAccountCreateRequestDto(
        @DecimalMin(value = "0.00", message = "balance is not valid, must at least be 0") BigDecimal balance,
        @NotBlank(message = "account holder can't be empty") String accountHolder) implements Serializable {
}