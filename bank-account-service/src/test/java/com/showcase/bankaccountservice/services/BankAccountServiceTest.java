package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.BankAccountMapper;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountUpdateRequestDto;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.repositories.BankAccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountMapper mapper;

    @InjectMocks
    private BankAccountService bankAccountService;

    private static BankAccountCreateRequestDto bankAccountCreateRequestDto;
    private static BankAccount bankAccount1;
    private static BankAccount bankAccount2;
    private static BankAccountResponseDto bankAccountResponseDto1;
    private static BankAccountResponseDto bankAccountResponseDto2;
    private static BankAccountUpdateRequestDto bankAccountUpdateRequestDto;
    private final static String ACCOUNTHOLDER1 = "HarryBanks";
    private final static String ID1 = "1111";
    private final static String ID2 = "2222";

    @BeforeAll
    static void setup() {
        BigDecimal amount = BigDecimal.valueOf(1000);
        BigDecimal amount2 = BigDecimal.valueOf(2000);
        bankAccountCreateRequestDto = new BankAccountCreateRequestDto(amount, ACCOUNTHOLDER1);
        bankAccountResponseDto1 = new BankAccountResponseDto(ID1, amount, ACCOUNTHOLDER1);
        bankAccountResponseDto2 = new BankAccountResponseDto(ID2, amount2, ACCOUNTHOLDER1);
        bankAccount1 = new BankAccount();
        bankAccount1.setId(ID1);
        bankAccount1.setAccountHolder(ACCOUNTHOLDER1);
        bankAccount1.setBalance(amount);
        bankAccount2 = new BankAccount();
        bankAccount2.setId(ID2);
        bankAccount2.setAccountHolder(ACCOUNTHOLDER1);
        bankAccount2.setBalance(amount2);
        bankAccountUpdateRequestDto = new BankAccountUpdateRequestDto(ID2, amount2, ACCOUNTHOLDER1);
    }

    @Test
    void createBankAccountAndShouldReturnValidBanksAccount() {
        // Prepare
        when(mapper.createRequestDtoToEntity(bankAccountCreateRequestDto)).thenReturn(bankAccount1);
        when(mapper.entityToReponseDto(bankAccount1)).thenReturn(bankAccountResponseDto1);
        when(bankAccountRepository.save(bankAccount1)).thenReturn(bankAccount1);

        // Perform
        BankAccountResponseDto result = bankAccountService.createBankAccount(bankAccountCreateRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(bankAccountResponseDto1, result);
    }

    @Test
    void readBankAccountByIdShouldReturnValidBankAccountResponseDto() throws EntityNotFoundException {
        //Prepare
        when(bankAccountRepository.findById(ID1)).thenReturn(Optional.of(bankAccount1));
        when(mapper.entityToReponseDto(bankAccount1)).thenReturn(bankAccountResponseDto1);

        //Perform
        BankAccountResponseDto result = bankAccountService.readBankAccountById(ID1);

        //Assert
        assertNotNull(result);
        assertEquals(bankAccountResponseDto1, result);
    }

    @Test
    void readBankAccountByIdShouldThrowEntityNotFoundException() throws EntityNotFoundException {
        //Prepare
        when(bankAccountRepository.findById(ID1)).thenReturn(Optional.empty());

        //Perform and Assert
        assertThrows(EntityNotFoundException.class, () -> bankAccountService.readBankAccountById(ID1));
    }

    @Test
    public void readBankAccountsByAccountHolderShouldReturnListWithAccountHolderResponseDtos() {
        // Prepare
        List<BankAccount> bankAccounts = Arrays.asList(bankAccount1, bankAccount2);

        when(bankAccountRepository.findByAccountHolder(ACCOUNTHOLDER1)).thenReturn(bankAccounts);
        when(mapper.entityToReponseDto(bankAccount1)).thenReturn(bankAccountResponseDto1);
        when(mapper.entityToReponseDto(bankAccount2)).thenReturn(bankAccountResponseDto2);

        // Perform
        List<BankAccountResponseDto> result = bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1);

        // Assert
        assertEquals(2, result.size());
        assertEquals(bankAccountResponseDto1, result.get(0));
        assertEquals(bankAccountResponseDto2, result.get(1));
    }

    @Test
    public void readBankAccountsByAccountHolderShouldReturnEmptyList() {
        // Prepare
        when(bankAccountRepository.findByAccountHolder(ACCOUNTHOLDER1)).thenReturn(List.of());

        // Perform
        List<BankAccountResponseDto> result = bankAccountService.readBankAccountsByAccountHolder(ACCOUNTHOLDER1);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void updateBankAccountShouldReturnUpdatedResponseDto() throws EntityNotFoundException {
        // Arrange
        when(bankAccountRepository.existsById(ID2)).thenReturn(true);
        when(mapper.updateRequestDtoToEntity(bankAccountUpdateRequestDto)).thenReturn(bankAccount2);
        when(bankAccountRepository.save(bankAccount2)).thenReturn(bankAccount2);
        when(mapper.entityToReponseDto(bankAccount2)).thenReturn(bankAccountResponseDto2);

        // Act
        BankAccountResponseDto result = bankAccountService.updateBankAccount(bankAccountUpdateRequestDto);

        // Assert
        assertEquals(bankAccountResponseDto2, result);
    }

    @Test
    public void updateBankAccountShouldThrownEntityNotFoundException() {
        // Arrange
        when(bankAccountRepository.existsById(bankAccountUpdateRequestDto.id())).thenReturn(false);

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> bankAccountService.updateBankAccount(bankAccountUpdateRequestDto));
    }

    @Test
    public void deleteBankAccountShouldBePerfomedOnce() throws EntityNotFoundException {
        // Arrange
        when(bankAccountRepository.existsById(ID2)).thenReturn(true);
        when(mapper.updateRequestDtoToEntity(bankAccountUpdateRequestDto)).thenReturn(bankAccount2);

        // Act
        bankAccountService.deleteBankAccount(bankAccountUpdateRequestDto);

        // Assert
        verify(bankAccountRepository).delete(bankAccount2);
    }

    @Test
    public void deleteBankAccountShouldThrowEntityNotFoundException() {
        // Arrange
        when(bankAccountRepository.existsById(bankAccountUpdateRequestDto.id())).thenReturn(false);

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> bankAccountService.deleteBankAccount(bankAccountUpdateRequestDto));
    }

    @Test
    public void deleteBankAccountByIdShouldBePerfomedOnce() throws EntityNotFoundException {
        // Arrange
        when(bankAccountRepository.existsById(ID2)).thenReturn(true);

        // Act
        bankAccountService.deleteBankAccount(ID2);

        // Assert
        verify(bankAccountRepository).deleteById(ID2);
    }

    @Test
    public void deleteBankAccountByIdShouldThrowEntityNotFoundException() {
        // Arrange
        when(bankAccountRepository.existsById(ID2)).thenReturn(false);

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> bankAccountService.deleteBankAccount(ID2));
    }
}