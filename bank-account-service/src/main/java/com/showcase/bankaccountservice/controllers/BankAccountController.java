package com.showcase.bankaccountservice.controllers;

import com.showcase.bankaccountservice.exceptions.EntityNotFoundException;
import com.showcase.bankaccountservice.model.dtos.BankAccountCreateRequestDto;
import com.showcase.bankaccountservice.model.dtos.BankAccountResponseDto;
import com.showcase.bankaccountservice.services.BankAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts")
@AllArgsConstructor
@Validated
public class BankAccountController {

    private BankAccountService bankAccountService;

    /**
     * Create BankAccount, provide both accountHolder and balance, Admin functionality
     * @param dto, BankAccountCreateRequestDto contains both accountHolder and amount
     * @return the created BankAccount Including the id
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/create-manual")
    public ResponseEntity<BankAccountResponseDto> createBankAccountManual(@RequestBody @Valid BankAccountCreateRequestDto dto){
        return new ResponseEntity<>(bankAccountService.createBankAccount(dto), HttpStatus.CREATED);
    }

    /**
     * Create BankAccount, it uses the authenticated user as accountHolder and sets the balance to zero
     * @return the created BankAccount Including the id
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @PutMapping("/create")
    public ResponseEntity<BankAccountResponseDto> createBankAccount(@AuthenticationPrincipal Jwt jwt){
        BankAccountResponseDto bankAccountResponseDto = bankAccountService.createNewBankAccount(jwt);
        return new ResponseEntity<>(bankAccountResponseDto, HttpStatus.CREATED);
    }

    //READ by accountholder
    @PreAuthorize("@securityMethods.userIsAccountHolder1(authentication, #accountHolder) OR hasRole('ROLE_ADMIN')")
    @GetMapping(value ="/read-by-accountholder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BankAccountResponseDto>> readBankAccounts(@RequestParam @NotBlank(message = "accountHolder must not be empty") String accountHolder){
        return new ResponseEntity<>(bankAccountService.readBankAccountsByAccountHolder(accountHolder), HttpStatus.OK);
    }

    //READ by bankAccount Id
    @PostAuthorize("@securityMethods.userIsAccountHolder2(returnObject, #jwt)  OR hasRole('ROLE_ADMIN')")
    @GetMapping("/read-by-id")
    public ResponseEntity<BankAccountResponseDto> readBankAccount(@RequestParam @NotBlank(message = "id must not be empty") String id, @AuthenticationPrincipal Jwt jwt) throws EntityNotFoundException {
        BankAccountResponseDto bankAccountResponseDto = bankAccountService.readBankAccountById(id);
        return new ResponseEntity<>(bankAccountResponseDto, HttpStatus.OK);
    }

    //DELETE by id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-by-id")
    public void deleteBankAccount(@NotBlank(message = "id must not be empty") String id) throws EntityNotFoundException {
        bankAccountService.deleteBankAccount(id);
    }
}
