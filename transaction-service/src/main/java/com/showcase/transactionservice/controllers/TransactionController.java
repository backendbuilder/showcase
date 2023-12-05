package com.showcase.transactionservice.controllers;

import com.showcase.transactionservice.model.dtos.TransactionRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.showcase.transactionservice.model.dtos.TransactionResponseDto;
import com.showcase.transactionservice.services.TransactionService;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("#jwt.getClaimAsString('preferred_username') == #dto.accountHolder() OR hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/create-transaction")
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody @Valid TransactionRequestDto dto, @AuthenticationPrincipal Jwt jwt){
        TransactionResponseDto transactionResponseDto = transactionService.initiateTransaction(dto);
        return new ResponseEntity<>(transactionResponseDto, HttpStatus.OK);
    }

/*    @GetMapping(value = "/list-transactions")
    public ResponseEntity<String> listTransactions(@RequestParam TransactionStatus status){

        System.out.println("dto = " + dto.toString());
        System.out.println("transaction = " + TransactionInitializationDtoMapper.INSTANCE.transactionRequestDtoToTransaction(dto).toString());
        producer.sendMessage(TransactionInitializationDtoMapper.INSTANCE.transactionRequestDtoToTransaction(dto).toString());

        return new ResponseEntity<>("hey!", HttpStatus.OK);


    }*/


}