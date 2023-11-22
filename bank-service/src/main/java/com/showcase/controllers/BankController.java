package com.showcase.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;*/
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/bank-service")
public class BankController {

    @GetMapping("token-info")
    public ResponseEntity<String> printTokenInfo(/*@AuthenticationPrincipal Jwt jwt*/){
/*
        System.out.println("subject (user-id): " + jwt.getSubject());
        System.out.println("username " + jwt.getClaimAsString("preferred_username"));
        System.out.println("claims: " + jwt.getClaims().toString());
        System.out.println("token: " + jwt.getTokenValue());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();System
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Name: " + authentication.getName());
        System.out.println("Credentials: " +authentication.getCredentials().toString());
        System.out.println("Authorities: ");
        authentication.getAuthorities().forEach((e) -> System.out.println(e.toString()));*/

        return new ResponseEntity<>("this is a secret", HttpStatus.OK);
        }
}
