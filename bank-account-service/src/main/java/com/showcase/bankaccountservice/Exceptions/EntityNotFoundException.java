package com.showcase.bankaccountservice.Exceptions;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
