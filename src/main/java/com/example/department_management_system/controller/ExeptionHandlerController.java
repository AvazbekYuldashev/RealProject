package com.example.department_management_system.controller;

import com.example.department_management_system.exp.AppBadRequestExeption;
import com.example.department_management_system.exp.ItemNotFoundExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExeptionHandlerController {


    @ExceptionHandler({ItemNotFoundExeption.class, AppBadRequestExeption.class})
    public ResponseEntity<String> handler(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Invalid application status. Allowed values: SENT, APPROVED, IN_PROGRESS, COMPLETED.");
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handlerRunTime(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

}

