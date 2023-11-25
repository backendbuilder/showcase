package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.BankAccountMapper;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountUpdateRequestDto;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private static final String NOT_FOUND = "Bank account not found";
    private final BankAccountMapper mapper;

    @Autowired
    public BankAccountService(BankAccountRepository repository, BankAccountMapper mapper){
        bankAccountRepository = repository;
        this.mapper = mapper;
    }

    //CREATE, provide account holder and amount
    public BankAccountResponseDto createBankAccount(BankAccountCreateRequestDto dto){
        BankAccount bankAccount = mapper.createRequestDtoToEntity(dto);
        bankAccount = bankAccountRepository.save(bankAccount);
        return mapper.entityToReponseDto(bankAccount);
    }

    //READ by id
    public BankAccountResponseDto readBankAccountById(String id) throws EntityNotFoundException {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
       return mapper.entityToReponseDto(bankAccount.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND)));
    }

    //READ by accountHolder
    public List<BankAccountResponseDto> readBankAccountsByAccountHolder(String accountHolder) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByAccountHolder(accountHolder);

        if(bankAccounts.isEmpty()) {
           return List.of();
        }
        return bankAccounts.stream()
                .map(mapper::entityToReponseDto)
                .toList();
    }

    //UPDATE
    public BankAccountResponseDto updateBankAccount(BankAccountUpdateRequestDto dto) throws EntityNotFoundException {
        if (bankAccountRepository.existsById(dto.id())) {
            BankAccount bankAccount = bankAccountRepository.save(mapper.updateRequestDtoToEntity(dto));
            return mapper.entityToReponseDto(bankAccount);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    //DELETE bank account
    public void deleteBankAccount(BankAccountUpdateRequestDto dto) throws EntityNotFoundException {
        if (bankAccountRepository.existsById(dto.id())) {
            bankAccountRepository.delete(mapper.updateRequestDtoToEntity(dto));
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    //DELETE by id
    public void deleteBankAccount(String id) throws EntityNotFoundException {
        if (bankAccountRepository.existsById(id)) {
            bankAccountRepository.deleteById(id);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    //CREATE NEW
    public BankAccountResponseDto createNewBankAccount(Jwt jwt) {
        String authenticatedUsername = jwt.getClaimAsString("preferred_username");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountHolder(authenticatedUsername);
        bankAccount.setBalance(BigDecimal.ZERO);
        return mapper.entityToReponseDto(bankAccountRepository.save(bankAccount));
    }
}
