package com.showcase.bankaccountservice.exceptions;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(Exception e) {
        return new ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        JsonObject jsonResponse = new JsonObject();
        JsonArray errors = new JsonArray();
        jsonResponse.add("errors", errors);
        e.getConstraintViolations().forEach(objectError -> errors.add(objectError.getMessage()));

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(jsonResponse.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        JsonObject jsonResponse = new JsonObject();
        JsonArray errors = new JsonArray();
        jsonResponse.add("errors", errors);
        e.getAllErrors().forEach( objectError -> errors.add(objectError.getDefaultMessage()));

       return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(jsonResponse.toString());
    }

}
