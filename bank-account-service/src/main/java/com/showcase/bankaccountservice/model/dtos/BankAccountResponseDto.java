package com.showcase.bankaccountservice.model.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.showcase.bankaccountservice.model.entities.BankAccount}
 */
public record BankAccountResponseDto(String id, BigDecimal balance, String accountHolder) implements Serializable {
}