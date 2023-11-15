package com.showcase.bankaccountservice.controllers;

import com.showcase.bankaccountservice.Exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.services.BankAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")
@AllArgsConstructor
@Setter
public class BankAccountController {

    private BankAccountService bankAccountService;

    /**
     * Create BankAccount, provide both accountHolder and balance, Admin functionality
     * @param dto, BankAccountCreateRequestDto contains both accountHolder and amount
     * @return the created BankAccount Including the id
     */

    //TODO if principal ADMIN

    @PutMapping("/create-manual")
    public ResponseEntity<BankAccountResponseDto> createBankAccountManual(@RequestBody @Valid BankAccountCreateRequestDto dto){
        return new ResponseEntity<>(bankAccountService.createBankAccount(dto), HttpStatus.CREATED);
    }

    /**
     * Create BankAccount, i uses the authenticated user as accountHolder and sets the balance to zero
     * @return the created BankAccount Including the id
     */
    //TODO if principal = authenticated
    @PutMapping("create")
    public ResponseEntity<BankAccountResponseDto> createBankAccount(){
        return new ResponseEntity<>(bankAccountService.createNewBankAccount(), HttpStatus.CREATED);
    }

    //READ by accountholder
    //TODO if principal = accountHolder or ADMIN
    @GetMapping("/read-by-accountholder")
    public ResponseEntity<List<BankAccountResponseDto>> readBankAccounts(@RequestParam @NotBlank String accountHolder){
        return new ResponseEntity<>(bankAccountService.readBankAccountsByAccountHolder(accountHolder), HttpStatus.OK);
    }

    //READ by bankAccount Id
    //TODO if principal = accountHolder or ADMIN
    @GetMapping("/read-by-id")
    public ResponseEntity<BankAccountResponseDto> readBankAccount(@RequestParam @NotBlank String id) throws EntityNotFoundException {
        return new ResponseEntity<>(bankAccountService.readBankAccountById(id), HttpStatus.OK);
    }

    //DELETE by id
    //TODO if principal = accountHolder or ADMIN
    @DeleteMapping("/delete-by-id")
    public void deleteBankAccount(@NotBlank String id) throws EntityNotFoundException {
        bankAccountService.deleteBankAccount(id);
    }
}
