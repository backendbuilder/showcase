package com.showcase.bankaccountservice.services;

import com.showcase.bankaccountservice.Exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.BankAccountMapper;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountUpdateRequestDto;
import com.showcase.bankaccountservice.model.entities.BankAccount;
import com.showcase.bankaccountservice.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {


    private final BankAccountRepository bankAccountRepository;
    private static final String NOT_FOUND = "Bank account not found";
    BankAccountMapper mapper;

    @Autowired
    public BankAccountService(BankAccountRepository repository, BankAccountMapper mapper){
        bankAccountRepository = repository;
        this.mapper = mapper;
    }

    //CREATE, provide account holder and amount
    public BankAccountResponseDto createBankAccount(BankAccountCreateRequestDto dto){
        BankAccount bankAccount = BankAccountMapper.MAPPER.createRequestDtoToEntity(dto);
        bankAccount = bankAccountRepository.save(bankAccount);
        return mapper.entityToReponseDto(bankAccount);
    }

    //READ by id
    public BankAccountResponseDto readBankAccountById(String id) throws EntityNotFoundException {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
       return BankAccountMapper.MAPPER.entityToReponseDto(bankAccount.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND)));
    }

    //READ by accountHolder
    public List<BankAccountResponseDto> readBankAccountsByAccountHolder(String accountHolder) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByAccountHolder(accountHolder);

        if(bankAccounts.isEmpty()) {
           return List.of();
        }
        return bankAccounts.stream()
                .map(BankAccountMapper.MAPPER::entityToReponseDto)
                .toList();
    }

    //UPDATE
    //TODO if principal - accountHolder or ADMIN
    public BankAccountResponseDto updateBankAccount(BankAccountUpdateRequestDto dto) throws EntityNotFoundException {
        if (bankAccountRepository.existsById(dto.id())) {
            BankAccount bankAccount = bankAccountRepository.save(BankAccountMapper.MAPPER.updateRequestDtoToEntity(dto));
            return BankAccountMapper.MAPPER.entityToReponseDto(bankAccount);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    //DELETE bank account

    public void deleteBankAccount(BankAccountUpdateRequestDto dto) throws EntityNotFoundException {
        if (bankAccountRepository.existsById(dto.id())) {
            bankAccountRepository.delete(BankAccountMapper.MAPPER.updateRequestDtoToEntity(dto));
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    //DELETE by id
    //TODO if principal - accountHolder or ADMIN
    public void deleteBankAccount(String id) throws EntityNotFoundException {
        if (bankAccountRepository.existsById(id)) {
            bankAccountRepository.deleteById(id);
        } else throw new EntityNotFoundException(NOT_FOUND);
    }

    //CREATE NEW
    public BankAccountResponseDto createNewBankAccount() {
        //(@AuthorizedPrincipal Jwt jwt)
        //BankAccount = new BankAccount
        //bankAccount.accountHolder = jwt.getSubject()
        //bankAccount.amount = 0
        //save

        return null;
    }
}
