package com.showcase.bankaccountservice.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.showcase.bankaccountservice.model.entities.BankAccount}
 */
public record BankAccountUpdateRequestDto(
        @NotNull(message = "id can't be null")
        @NotBlank(message = "id can't be empty")
        String id,

        @NotNull(message = "balance can't be null")
        BigDecimal balance,

        @NotNull(message = "accountHolder can't be null")
        @NotBlank(message = "accountHolder can't be blank")
        String accountHolder

) implements Serializable {}