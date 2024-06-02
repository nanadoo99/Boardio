package com.nki.t1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class MyContollerAdvice {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> MyExceptionHander(Exception ex, Model model) {
//        String errorMessage = "Resource not found: " + ex.getMessage();
//        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> NoHandlerFoundExceptionHandler(NoHandlerFoundException ex, Model model) {
    public String NoHandlerFoundExceptionHandler(NoHandlerFoundException ex, Model model) {
        String errorMessage = "NoHandlerFoundExceptionHandler: " + ex.getMessage();
        System.out.println("@@@@@ NoHandlerFoundExceptionHandler");
//        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        return "error1";
    }
}
