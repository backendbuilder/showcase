package com.showcase.bankaccountservice.model.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.showcase.bankaccountservice.model.entities.BankAccount}
 */
public record BankAccountCreateRequestDto(
        @NotNull BigDecimal balance,
        @NotEmpty String accountHolder) implements Serializable {
}