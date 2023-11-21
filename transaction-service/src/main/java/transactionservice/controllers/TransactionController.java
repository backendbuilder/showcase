package transactionservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.model.dtos.TransactionResponseDto;
import transactionservice.services.TransactionService;

@RestController
@AllArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping(value = "/create-transaction")
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody TransactionRequestDto dto){
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
