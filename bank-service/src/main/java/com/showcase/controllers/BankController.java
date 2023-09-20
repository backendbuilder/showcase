package com.showcase.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class BankController {

    @GetMapping()
    public ResponseEntity<String> getBankInfo(){


        return new ResponseEntity<>("this is a secret", HttpStatus.OK);
    }

}
