package transactionservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transactionservice.kafka.Producer;
import transactionservice.model.dtos.mappers.TransactionMapper;
import transactionservice.model.dtos.TransactionRequestDto;
import transactionservice.services.TransactionService;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction-service")
public class TransactionController {

    private final Producer producer;
     private final TransactionService transactionService;
     private final TransactionMapper transactionMapper;

    @PostMapping(value = "/create-transaction")
    public ResponseEntity<String> createTransaction(@RequestBody TransactionRequestDto dto){
        //ToDO remove println
        System.out.println("dto = " + dto.toString());
        System.out.println("transaction = " + transactionMapper.transactionRequestDtoToTransaction(dto).toString());

        transactionService.initiateTransaction(dto);
        //producer.sendMessage(TransactionInitializationDtoMapper.INSTANCE.transactionToPendingTransactionDto(dto).toString());

        return new ResponseEntity<>("hey!", HttpStatus.OK);


    }
/*    @GetMapping(value = "/list-transactions")
    public ResponseEntity<String> listTransactions(@RequestParam TransactionStatus status){

        System.out.println("dto = " + dto.toString());
        System.out.println("transaction = " + TransactionInitializationDtoMapper.INSTANCE.transactionRequestDtoToTransaction(dto).toString());
        producer.sendMessage(TransactionInitializationDtoMapper.INSTANCE.transactionRequestDtoToTransaction(dto).toString());

        return new ResponseEntity<>("hey!", HttpStatus.OK);


    }*/


}
