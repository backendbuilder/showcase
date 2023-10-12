package transactionservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transactionservice.Producer;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction-service")
public class TransactionController {

    private final Producer producer;

    @PostMapping(value = "/make-transaction")
    public ResponseEntity<String> makeTransaction(@RequestParam String message){

        System.out.println("controller::makeTransaction()");
        producer.sendMessage(message);

        return new ResponseEntity<>("hey!", HttpStatus.OK);
    }


}
