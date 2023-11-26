package transactionservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.TransactionResponseDto;
import transactionservice.services.TransactionService;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("#jwt.getClaimAsString('preferred_username') == #dto.accountHolder() OR hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/create-transaction")
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TransactionRequestDto dto, @AuthenticationPrincipal Jwt jwt){
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
